package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSession;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.infrastructure.client.WechatPhoneGateway;
import com.muhou.backend.infrastructure.client.WechatPhoneNumberResult;
import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import com.muhou.backend.infrastructure.persistence.mapper.UserMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.web.request.UserProfileUpdateRequest;
import com.muhou.backend.web.response.AdminUserResponse;
import com.muhou.backend.web.response.UserProfileResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserApplicationService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final CurrentUserSupport currentUserSupport;
    private final FactoryOnboardingApplicationService factoryOnboardingApplicationService;
    private final WechatPhoneGateway wechatPhoneGateway;

    public UserApplicationService(UserMapper userMapper,
                                  UserRoleMapper userRoleMapper,
                                  CurrentUserSupport currentUserSupport,
                                  FactoryOnboardingApplicationService factoryOnboardingApplicationService,
                                  WechatPhoneGateway wechatPhoneGateway) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.currentUserSupport = currentUserSupport;
        this.factoryOnboardingApplicationService = factoryOnboardingApplicationService;
        this.wechatPhoneGateway = wechatPhoneGateway;
    }

    public UserProfileResponse getCurrentUser(String ignoredRole) {
        CurrentUserSession session = currentUserSupport.getCurrentSession();
        if (session == null || session.getUserId() == null) {
            throw new BizException(ResultCode.UNAUTHORIZED, "当前未登录，请先从角色页进入系统");
        }

        UserEntity user = userMapper.selectById(session.getUserId());
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到当前登录用户");
        }

        FactoryOnboardingApplicationService.LoginSnapshot snapshot =
            factoryOnboardingApplicationService.buildLoginSnapshot(user, session.getRole());

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setPhone(user.getPhone());
        response.setCurrentRole(snapshot.getCurrentRole());
        response.setRoleBindings(snapshot.getRoleBindings());
        response.setRegisterStatus(snapshot.getRegisterStatus());
        response.setDefaultEntry(snapshot.getDefaultEntry());
        response.setNeedRoleSelection(snapshot.isNeedRoleSelection());
        response.setFactoryAuditStatus(snapshot.getFactoryAuditStatus());
        response.setFactoryAuditRejectReason(snapshot.getFactoryAuditRejectReason());
        response.setRealnameVerified(user.getRealnameVerified() != null && user.getRealnameVerified() == 1);
        response.setStudentVerified(user.getStudentVerified() != null && user.getStudentVerified() == 1);
        return response;
    }

    public UserProfileResponse updateCurrentUserProfile(UserProfileUpdateRequest request) {
        Long userId = currentUserSupport.requireCurrentUserId();
        UserEntity user = requireUser(userId);
        String nickname = normalizeNickname(request.getNickname(), user.getNickname());
        String avatarUrl = normalizeAvatarUrl(request.getAvatarUrl(), user.getAvatarUrl());
        userMapper.updateProfile(userId, nickname, avatarUrl);
        return getCurrentUser(null);
    }

    public UserProfileResponse bindCurrentUserWechatPhone(String code) {
        Long userId = currentUserSupport.requireCurrentUserId();
        WechatPhoneNumberResult result = wechatPhoneGateway.getPhoneNumber(code);
        userMapper.updatePhone(userId, result.getPhoneNumber());
        return getCurrentUser(null);
    }

    public List<AdminUserResponse> listAdminUsers() {
        return userMapper.selectAdminList().stream()
            .map(this::toAdminUserResponse)
            .collect(Collectors.toList());
    }

    public Long resolveAdminUserId() {
        return currentUserSupport.requireCurrentUserId();
    }

    public void bindRoleToUser(Long userId, String role) {
        UserEntity user = requireUser(userId);
        List<String> roles = userRoleMapper.selectRoleCodesByUserId(userId);
        String normalizedRole = normalizeRole(role);

        if ("demander".equals(normalizedRole)) {
            if (roles.contains("supplier") || roles.contains("admin")) {
                throw new BizException(ResultCode.CONFLICT, "当前用户已绑定工厂或管理员角色，不能再绑定租赁方");
            }
            userRoleMapper.insertIgnore(userId, "demander");
            userMapper.updateRegisterStatus(userId, "active");
            return;
        }

        if (roles.contains("demander")) {
            throw new BizException(ResultCode.CONFLICT, "当前用户已绑定租赁方角色，不能再绑定工厂或管理员角色");
        }

        if ("admin".equals(normalizedRole)) {
            userRoleMapper.insertIgnore(userId, "supplier");
            userRoleMapper.insertIgnore(userId, "admin");
        } else {
            userRoleMapper.insertIgnore(userId, normalizedRole);
        }
        userMapper.updateRegisterStatus(user.getId(), "active");
    }

    public void unbindRoleFromUser(Long userId, String role) {
        UserEntity user = requireUser(userId);
        String normalizedRole = normalizeRole(role);
        List<String> roles = userRoleMapper.selectRoleCodesByUserId(user.getId());

        if ("demander".equals(normalizedRole)) {
            throw new BizException(ResultCode.CONFLICT, "租赁端基础角色不支持解绑");
        }
        if ("supplier".equals(normalizedRole) && roles.contains("admin")) {
            throw new BizException(ResultCode.CONFLICT, "管理员账号必须同时保留 supplier 角色");
        }
        if ("admin".equals(normalizedRole)) {
            throw new BizException(ResultCode.CONFLICT, "当前版本不支持直接解绑管理员角色");
        }
        userRoleMapper.disableRole(userId, normalizedRole);
    }

    private UserEntity requireUser(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "目标用户不存在");
        }
        return user;
    }

    private AdminUserResponse toAdminUserResponse(UserEntity user) {
        List<String> roleBindings = userRoleMapper.selectRoleCodesByUserId(user.getId());
        AdminUserResponse response = new AdminUserResponse();
        response.setId(user.getId());
        response.setName(user.getNickname());
        response.setPhone(user.getPhone());
        response.setRole(roleBindings.isEmpty() ? "" : roleBindings.get(0));
        response.setRoleBindings(roleBindings);

        boolean verified = (user.getRealnameVerified() != null && user.getRealnameVerified() == 1)
            || (user.getStudentVerified() != null && user.getStudentVerified() == 1);
        response.setVerified(verified);
        response.setVerifiedText(verified ? "已认证" : "未认证");
        response.setStatus(user.getRegisterStatus());
        response.setStatusText(userStatusText(user.getRegisterStatus()));
        return response;
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "demander";
        }
        String normalized = role.trim().toLowerCase();
        if (!List.of("demander", "supplier", "admin").contains(normalized)) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "不支持的角色类型: " + role);
        }
        return normalized;
    }

    private String normalizeNickname(String value, String fallback) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isBlank()) {
            return fallback == null || fallback.isBlank() ? "微信用户" : fallback;
        }
        return normalized.length() > 64 ? normalized.substring(0, 64) : normalized;
    }

    private String normalizeAvatarUrl(String value, String fallback) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isBlank()) {
            return fallback == null || fallback.isBlank() ? "/images/avatar.png" : fallback;
        }
        return normalized.length() > 500 ? normalized.substring(0, 500) : normalized;
    }

    private String userStatusText(String status) {
        return switch (status) {
            case "factory_pending" -> "工厂审核中";
            case "factory_rejected" -> "工厂申请已驳回";
            case "disabled" -> "已停用";
            case "new" -> "待选择角色";
            default -> "正常";
        };
    }
}
