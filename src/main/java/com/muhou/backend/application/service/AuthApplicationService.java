package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.config.properties.MuhouAppProperties;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.infrastructure.client.AuthLoginResult;
import com.muhou.backend.infrastructure.client.WechatAuthGateway;
import com.muhou.backend.infrastructure.persistence.entity.AdminAccountEntity;
import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import com.muhou.backend.infrastructure.persistence.entity.UserWechatEntity;
import com.muhou.backend.infrastructure.persistence.mapper.AdminAccountMapper;
import com.muhou.backend.infrastructure.persistence.mapper.FactoryAuditMapper;
import com.muhou.backend.infrastructure.persistence.mapper.FactoryProfileMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserWechatMapper;
import com.muhou.backend.web.response.AuthLoginResponse;
import com.muhou.backend.web.response.DemoAccountResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class AuthApplicationService {

    private static final DemoAccountSpec DEMANDER_DEMO = new DemoAccountSpec(
        "demander_demo",
        1L,
        "租赁端演示账号",
        "已绑定 demander，可直接体验租赁端完整下单流程"
    );

    private static final DemoAccountSpec ADMIN_DEMO = new DemoAccountSpec(
        "admin_demo",
        3L,
        "管理员演示账号",
        "已绑定 admin + supplier，可进入管理员端并切换工厂视角"
    );

    private static final DemoAccountSpec FACTORY_DEMO_A = new DemoAccountSpec(
        "factory_demo_a",
        4L,
        "factory_demo_a",
        "supplier only; factory A for split-order demo"
    );

    private static final DemoAccountSpec FACTORY_DEMO_B = new DemoAccountSpec(
        "factory_demo_b",
        5L,
        "factory_demo_b",
        "supplier only; factory B for split-order demo"
    );

    private static final DemoAccountSpec NEW_USER_DEMO = new DemoAccountSpec(
        "new_user_demo",
        6L,
        "新用户演示账号",
        "首次微信登录用户，无任何业务角色，可走租赁绑定或工厂邀请码入驻流程"
    );

    private final WechatAuthGateway wechatAuthGateway;
    private final MuhouAppProperties properties;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserWechatMapper userWechatMapper;
    private final FactoryProfileMapper factoryProfileMapper;
    private final FactoryAuditMapper factoryAuditMapper;
    private final AdminAccountMapper adminAccountMapper;
    private final CurrentUserSupport currentUserSupport;
    private final FactoryOnboardingApplicationService factoryOnboardingApplicationService;

    public AuthApplicationService(WechatAuthGateway wechatAuthGateway,
                                  MuhouAppProperties properties,
                                  UserMapper userMapper,
                                  UserRoleMapper userRoleMapper,
                                  UserWechatMapper userWechatMapper,
                                  FactoryProfileMapper factoryProfileMapper,
                                  FactoryAuditMapper factoryAuditMapper,
                                  AdminAccountMapper adminAccountMapper,
                                  CurrentUserSupport currentUserSupport,
                                  FactoryOnboardingApplicationService factoryOnboardingApplicationService) {
        this.wechatAuthGateway = wechatAuthGateway;
        this.properties = properties;
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.userWechatMapper = userWechatMapper;
        this.factoryProfileMapper = factoryProfileMapper;
        this.factoryAuditMapper = factoryAuditMapper;
        this.adminAccountMapper = adminAccountMapper;
        this.currentUserSupport = currentUserSupport;
        this.factoryOnboardingApplicationService = factoryOnboardingApplicationService;
    }

    public AuthLoginResponse login(String code, String desiredRole) {
        return login(code, desiredRole, false);
    }

    public AuthLoginResponse login(String code, String desiredRole, boolean forceRealWechat) {
        AuthLoginResult result = wechatAuthGateway.login(code, forceRealWechat);
        UserEntity user = bindWechatUser(result);
        FactoryOnboardingApplicationService.LoginSnapshot snapshot =
            factoryOnboardingApplicationService.buildLoginSnapshot(user, desiredRole);
        return buildAuthLoginResponse(user, result, snapshot);
    }

    public List<DemoAccountResponse> listDemoAccounts() {
        return List.of(
            toDemoAccountResponse(DEMANDER_DEMO),
            toDemoAccountResponse(FACTORY_DEMO_A),
            toDemoAccountResponse(FACTORY_DEMO_B),
            toDemoAccountResponse(ADMIN_DEMO),
            toDemoAccountResponse(NEW_USER_DEMO)
        );
    }

    public AuthLoginResponse demoLogin(String accountKey, String role, boolean resetDemo) {
        if (!properties.getAuth().isDemoLoginEnabled()) {
            throw new BizException(ResultCode.FORBIDDEN, "当前环境已关闭 demo 登录");
        }

        DemoAccountSpec accountSpec = resolveDemoAccount(accountKey);
        UserEntity user = userMapper.selectById(accountSpec.userId());
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "演示账号不存在，请先执行最新数据库脚本");
        }
        user = resetNewUserDemoIfNeeded(accountSpec, user, resetDemo);

        FactoryOnboardingApplicationService.LoginSnapshot snapshot =
            factoryOnboardingApplicationService.buildLoginSnapshot(user, normalizeRole(role));

        AuthLoginResult mockResult = new AuthLoginResult(
            "demo-openid-" + accountSpec.accountKey(),
            null,
            "demo-session-" + accountSpec.accountKey(),
            null,
            true
        );
        return buildAuthLoginResponse(user, mockResult, snapshot);
    }

    public AuthLoginResponse adminLogin(String username, String password) {
        String normalizedUsername = username == null ? "" : username.trim();
        AdminAccountEntity account = adminAccountMapper.selectByUsername(normalizedUsername);
        if (account == null || account.getEnabled() == null || account.getEnabled() != 1) {
            throw new BizException(ResultCode.UNAUTHORIZED, "管理员账号或密码错误");
        }
        if (!new BCryptPasswordEncoder().matches(password == null ? "" : password, account.getPasswordHash())) {
            throw new BizException(ResultCode.UNAUTHORIZED, "管理员账号或密码错误");
        }
        List<String> roles = userRoleMapper.selectRoleCodesByUserId(account.getUserId());
        if (!roles.contains("admin")) {
            throw new BizException(ResultCode.FORBIDDEN, "该账号未绑定管理员角色");
        }
        UserEntity user = userMapper.selectById(account.getUserId());
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "管理员用户不存在");
        }
        adminAccountMapper.updateLastLogin(account.getId());

        AuthLoginResponse response = new AuthLoginResponse();
        response.setToken(currentUserSupport.generateToken(user.getId(), "admin"));
        response.setUserId(user.getId());
        response.setCurrentRole("admin");
        response.setRoleBindings(roles);
        response.setRegisterStatus(user.getRegisterStatus());
        response.setDefaultEntry("admin_home");
        response.setNeedRoleSelection(false);
        response.setMock(false);
        return response;
    }

    private AuthLoginResponse buildAuthLoginResponse(UserEntity user,
                                                     AuthLoginResult authLoginResult,
                                                     FactoryOnboardingApplicationService.LoginSnapshot snapshot) {
        AuthLoginResponse response = new AuthLoginResponse();
        response.setToken(currentUserSupport.generateToken(user.getId(), snapshot.getCurrentRole() == null ? "" : snapshot.getCurrentRole()));
        response.setUserId(user.getId());
        response.setOpenId(authLoginResult.getOpenId());
        response.setSessionKey(authLoginResult.getSessionKey());
        response.setCurrentRole(snapshot.getCurrentRole());
        response.setRoleBindings(snapshot.getRoleBindings());
        response.setRegisterStatus(snapshot.getRegisterStatus());
        response.setDefaultEntry(snapshot.getDefaultEntry());
        response.setNeedRoleSelection(snapshot.isNeedRoleSelection());
        response.setFactoryAuditStatus(snapshot.getFactoryAuditStatus());
        response.setFactoryAuditRejectReason(snapshot.getFactoryAuditRejectReason());
        response.setMock(authLoginResult.isMock());
        return response;
    }

    private DemoAccountResponse toDemoAccountResponse(DemoAccountSpec spec) {
        UserEntity user = userMapper.selectById(spec.userId());
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "演示账号不存在，请先执行最新数据库脚本");
        }

        DemoAccountResponse response = new DemoAccountResponse();
        response.setAccountKey(spec.accountKey());
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setDescription(spec.description());
        response.setRoleBindings(factoryOnboardingApplicationService.buildLoginSnapshot(user, null).getRoleBindings());
        response.setRegisterStatus(user.getRegisterStatus());
        return response;
    }

    private DemoAccountSpec resolveDemoAccount(String accountKey) {
        if (accountKey == null || accountKey.isBlank()) {
            return DEMANDER_DEMO;
        }
        return switch (accountKey.trim().toLowerCase(Locale.ROOT)) {
            case "demander_demo" -> DEMANDER_DEMO;
            case "factory_demo_a" -> FACTORY_DEMO_A;
            case "factory_demo_b" -> FACTORY_DEMO_B;
            case "admin_demo" -> ADMIN_DEMO;
            case "new_user_demo" -> NEW_USER_DEMO;
            default -> throw new BizException(ResultCode.VALIDATION_ERROR, "不支持的演示账号: " + accountKey);
        };
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return null;
        }
        String normalized = role.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "supplier", "admin", "demander" -> normalized;
            default -> null;
        };
    }

    private UserEntity resetNewUserDemoIfNeeded(DemoAccountSpec accountSpec, UserEntity user, boolean resetDemo) {
        if (!"new_user_demo".equals(accountSpec.accountKey()) || !resetDemo) {
            return user;
        }

        List<String> roles = userRoleMapper.selectRoleCodesByUserId(user.getId());
        factoryProfileMapper.deleteByOwnerUserId(user.getId());
        factoryAuditMapper.deleteByApplicantUserId(user.getId());
        if (!roles.isEmpty() || !"new".equals(user.getRegisterStatus())) {
            userRoleMapper.disableAllRoles(user.getId());
            userMapper.updateRegisterStatus(user.getId(), "new");
            return userMapper.selectById(user.getId());
        }
        return user;
    }

    private UserEntity bindWechatUser(AuthLoginResult result) {
        UserWechatEntity binding = userWechatMapper.selectByOpenid(result.getOpenId());
        if (binding != null) {
            binding.setSessionKey(result.getSessionKey());
            binding.setUnionid(result.getUnionId());
            userWechatMapper.updateSession(binding);
            UserEntity user = userMapper.selectById(binding.getUserId());
            if (user != null) {
                return user;
            }
        }

        UserEntity user = new UserEntity();
        user.setNickname(buildWechatNickname(result.getOpenId()));
        user.setAvatarUrl("/images/avatar.png");
        user.setPhone(null);
        user.setRealnameVerified(0);
        user.setStudentVerified(0);
        user.setUserStatus("active");
        user.setRegisterStatus("new");
        userMapper.insert(user);

        UserWechatEntity entity = new UserWechatEntity();
        entity.setUserId(user.getId());
        entity.setOpenid(result.getOpenId());
        entity.setUnionid(result.getUnionId());
        entity.setSessionKey(result.getSessionKey());
        userWechatMapper.insert(entity);
        return user;
    }

    private String buildWechatNickname(String openId) {
        if (openId == null || openId.length() < 6) {
            return "微信用户";
        }
        return "微信用户" + openId.substring(openId.length() - 6);
    }

    private record DemoAccountSpec(String accountKey, Long userId, String label, String description) {
    }
}
