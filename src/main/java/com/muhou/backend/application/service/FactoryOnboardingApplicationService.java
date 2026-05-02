package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.common.util.TimeUtils;
import com.muhou.backend.infrastructure.persistence.entity.FactoryAuditEntity;
import com.muhou.backend.infrastructure.persistence.entity.FactoryInviteCodeEntity;
import com.muhou.backend.infrastructure.persistence.entity.FactoryProfileEntity;
import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import com.muhou.backend.infrastructure.persistence.mapper.FactoryAuditMapper;
import com.muhou.backend.infrastructure.persistence.mapper.FactoryInviteCodeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.FactoryProfileMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.web.request.FactoryApplicationSubmitRequest;
import com.muhou.backend.web.response.FactoryAuditDetailResponse;
import com.muhou.backend.web.response.FactoryAuditResponse;
import com.muhou.backend.web.response.FactoryAuditStatusResponse;
import com.muhou.backend.web.response.FactoryInviteCodeResponse;
import com.muhou.backend.web.response.FactoryInviteCreateResponse;
import com.muhou.backend.web.response.FactoryInviteVerifyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class FactoryOnboardingApplicationService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final FactoryAuditMapper factoryAuditMapper;
    private final FactoryInviteCodeMapper factoryInviteCodeMapper;
    private final FactoryProfileMapper factoryProfileMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final CurrentUserSupport currentUserSupport;

    public FactoryOnboardingApplicationService(FactoryAuditMapper factoryAuditMapper,
                                               FactoryInviteCodeMapper factoryInviteCodeMapper,
                                               FactoryProfileMapper factoryProfileMapper,
                                               UserMapper userMapper,
                                               UserRoleMapper userRoleMapper,
                                               CurrentUserSupport currentUserSupport) {
        this.factoryAuditMapper = factoryAuditMapper;
        this.factoryInviteCodeMapper = factoryInviteCodeMapper;
        this.factoryProfileMapper = factoryProfileMapper;
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.currentUserSupport = currentUserSupport;
    }

    public LoginSnapshot buildLoginSnapshot(UserEntity user, String desiredRole) {
        List<String> roleBindings = userRoleMapper.selectRoleCodesByUserId(user.getId());
        ensureAdminHasSupplier(roleBindings);

        String currentRole = resolveCurrentRole(roleBindings, desiredRole);
        FactoryAuditEntity latestAudit = factoryAuditMapper.selectLatestByApplicantUserId(user.getId());
        String registerStatus = normalizeRegisterStatus(user.getRegisterStatus());
        String defaultEntry = resolveDefaultEntry(roleBindings, registerStatus, latestAudit, currentRole);

        LoginSnapshot snapshot = new LoginSnapshot();
        snapshot.setCurrentRole(currentRole);
        snapshot.setRoleBindings(roleBindings);
        snapshot.setRegisterStatus(registerStatus);
        snapshot.setDefaultEntry(defaultEntry);
        snapshot.setNeedRoleSelection("role_select".equals(defaultEntry));
        if (latestAudit != null) {
            snapshot.setFactoryAuditStatus(latestAudit.getAuditStatus());
            snapshot.setFactoryAuditRejectReason(latestAudit.getRejectReason());
        }
        return snapshot;
    }

    @Transactional
    public LoginSnapshot bindDemanderForCurrentUser() {
        Long userId = currentUserSupport.requireCurrentUserId();
        UserEntity user = requireUser(userId);
        List<String> roleBindings = userRoleMapper.selectRoleCodesByUserId(userId);
        if (roleBindings.contains("supplier") || roleBindings.contains("admin")) {
            throw new BizException(ResultCode.CONFLICT, "当前账号已绑定工厂或管理员角色，不能再绑定租赁方");
        }
        userRoleMapper.insertIgnore(userId, "demander");
        userMapper.updateRegisterStatus(userId, "active");
        UserEntity latestUser = requireUser(userId);
        return buildLoginSnapshot(latestUser, "demander");
    }

    @Transactional
    public FactoryInviteVerifyResponse verifyInviteCodeForCurrentUser(String rawCode) {
        Long userId = currentUserSupport.requireCurrentUserId();
        UserEntity user = requireUser(userId);
        List<String> roles = userRoleMapper.selectRoleCodesByUserId(userId);
        ensureCanApplyFactory(user, roles);

        FactoryInviteCodeEntity inviteCode = requireInviteCode(rawCode);
        validateInviteCodeAvailability(inviteCode, userId);

        if ("locked".equals(inviteCode.getStatus()) && userId.equals(inviteCode.getLockedByUserId())) {
            return toFactoryInviteVerifyResponse(inviteCode);
        }

        int updated = factoryInviteCodeMapper.lockByUser(inviteCode.getId(), userId);
        if (updated == 0) {
            FactoryInviteCodeEntity latest = factoryInviteCodeMapper.selectById(inviteCode.getId());
            validateInviteCodeAvailability(latest, userId);
        }

        return toFactoryInviteVerifyResponse(factoryInviteCodeMapper.selectById(inviteCode.getId()));
    }

    @Transactional
    public FactoryAuditStatusResponse submitFactoryApplicationForCurrentUser(FactoryApplicationSubmitRequest request) {
        Long userId = currentUserSupport.requireCurrentUserId();
        UserEntity user = requireUser(userId);
        List<String> roles = userRoleMapper.selectRoleCodesByUserId(userId);
        ensureCanApplyFactory(user, roles);
        if (factoryAuditMapper.countPendingByApplicantUserId(userId) > 0) {
            throw new BizException(ResultCode.CONFLICT, "当前账号已有待审核的工厂入驻申请");
        }

        FactoryInviteCodeEntity inviteCode = requireLockedInviteCode(userId);

        FactoryAuditEntity entity = new FactoryAuditEntity();
        entity.setUserId(userId);
        entity.setApplicantUserId(userId);
        entity.setInviteCodeId(inviteCode.getId());
        entity.setCompanyName(request.getFactoryName());
        entity.setFactoryName(request.getFactoryName());
        entity.setUnifiedSocialCreditCode(request.getUnifiedSocialCreditCode().trim().toUpperCase(Locale.ROOT));
        entity.setBusinessLicenseUrl(request.getBusinessLicenseUrl());
        entity.setContactName(request.getContactName());
        entity.setContactPhone(request.getContactPhone());
        entity.setFactoryAddress(request.getFactoryAddress());
        entity.setMainBusiness(request.getMainBusiness());
        entity.setRemark(request.getRemark());
        entity.setAuditStatus("pending");
        factoryAuditMapper.insert(entity);

        userMapper.updateRegisterStatus(userId, "factory_pending");
        return toFactoryAuditStatusResponse(factoryAuditMapper.selectById(entity.getId()));
    }

    public FactoryAuditStatusResponse getCurrentFactoryAuditStatus() {
        Long userId = currentUserSupport.requireCurrentUserId();
        FactoryAuditEntity entity = factoryAuditMapper.selectLatestByApplicantUserId(userId);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "当前账号暂无工厂入驻申请记录");
        }
        return toFactoryAuditStatusResponse(entity);
    }

    public List<FactoryInviteCodeResponse> listInviteCodes() {
        requireAdminRole();
        return factoryInviteCodeMapper.selectAll().stream()
            .map(this::toFactoryInviteCodeResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public FactoryInviteCreateResponse createInviteCode(Integer expireDays, String remark) {
        Long adminUserId = requireAdminRole();
        int normalizedExpireDays = expireDays == null || expireDays < 1 ? 7 : Math.min(expireDays, 30);
        String inviteCodePlain = buildInviteCode();

        FactoryInviteCodeEntity entity = new FactoryInviteCodeEntity();
        entity.setCodeHash(hashInviteCode(inviteCodePlain));
        entity.setCodePlain(inviteCodePlain);
        entity.setCodeSuffix(inviteCodePlain.substring(inviteCodePlain.length() - 6));
        entity.setStatus("unused");
        entity.setExpireAt(LocalDateTime.now().plusDays(normalizedExpireDays));
        entity.setCreatedByAdminUserId(adminUserId);
        entity.setRemark(remark);
        factoryInviteCodeMapper.insert(entity);

        FactoryInviteCreateResponse response = new FactoryInviteCreateResponse();
        response.setId(entity.getId());
        response.setInviteCode(inviteCodePlain);
        response.setCodeSuffix(entity.getCodeSuffix());
        response.setExpireAt(TimeUtils.format(entity.getExpireAt()));
        response.setRemark(entity.getRemark());
        return response;
    }

    @Transactional
    public List<FactoryInviteCodeResponse> revokeInviteCode(Long id) {
        Long adminUserId = requireAdminRole();
        FactoryInviteCodeEntity entity = factoryInviteCodeMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到邀请码记录");
        }
        factoryInviteCodeMapper.revoke(id, adminUserId);
        return listInviteCodes();
    }

    public List<FactoryAuditResponse> listFactoryAudits() {
        requireAdminRole();
        return factoryAuditMapper.selectPendingList().stream()
            .map(this::toFactoryAuditResponse)
            .collect(Collectors.toList());
    }

    public List<FactoryAuditResponse> listFactoryAuditHistory() {
        requireAdminRole();
        return factoryAuditMapper.selectHistoryList().stream()
            .map(this::toFactoryAuditResponse)
            .collect(Collectors.toList());
    }

    public FactoryAuditDetailResponse getFactoryAuditDetail(Long id) {
        requireAdminRole();
        FactoryAuditEntity entity = factoryAuditMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到工厂入驻审核记录");
        }
        return toFactoryAuditDetailResponse(entity);
    }

    @Transactional
    public List<FactoryAuditResponse> reviewFactoryAudit(Long id, boolean approved, String remark) {
        Long adminUserId = requireAdminRole();
        FactoryAuditEntity entity = factoryAuditMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到工厂入驻审核记录");
        }
        if (!"pending".equals(entity.getAuditStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该工厂入驻审核已处理，不能重复审核");
        }

        UserEntity applicant = requireUser(entity.getApplicantUserId());
        List<String> applicantRoles = userRoleMapper.selectRoleCodesByUserId(applicant.getId());
        if (applicantRoles.contains("demander")) {
            throw new BizException(ResultCode.CONFLICT, "申请用户已绑定租赁方角色，不能通过工厂入驻审核");
        }

        Long createdFactoryId = null;
        if (approved) {
            if (factoryProfileMapper.selectByOwnerUserId(applicant.getId()) != null) {
                throw new BizException(ResultCode.CONFLICT, "该申请用户已绑定正式工厂主体");
            }
            if (factoryProfileMapper.selectByUnifiedSocialCreditCode(entity.getUnifiedSocialCreditCode()) != null) {
                throw new BizException(ResultCode.CONFLICT, "统一社会信用代码已被其他工厂使用");
            }
            FactoryProfileEntity profile = new FactoryProfileEntity();
            profile.setOwnerUserId(applicant.getId());
            profile.setFactoryName(entity.getFactoryName());
            profile.setUnifiedSocialCreditCode(entity.getUnifiedSocialCreditCode());
            profile.setBusinessLicenseUrl(entity.getBusinessLicenseUrl());
            profile.setContactName(entity.getContactName());
            profile.setContactPhone(entity.getContactPhone());
            profile.setFactoryAddress(entity.getFactoryAddress());
            profile.setMainBusiness(entity.getMainBusiness());
            profile.setStatus("active");
            profile.setApprovedAuditId(entity.getId());
            profile.setApprovedByAdminUserId(adminUserId);
            profile.setApprovedAt(LocalDateTime.now());
            factoryProfileMapper.insert(profile);
            createdFactoryId = profile.getId();
        }

        int updated = factoryAuditMapper.updateReview(
            id,
            approved ? "approved" : "rejected",
            approved ? remark : null,
            approved ? null : blankToNull(remark),
            adminUserId,
            createdFactoryId
        );
        if (updated == 0) {
            throw new BizException(ResultCode.CONFLICT, "该工厂入驻审核已处理，不能重复审核");
        }

        if (approved) {
            userRoleMapper.insertIgnore(applicant.getId(), "supplier");
            userMapper.updateRegisterStatus(applicant.getId(), "active");
        } else {
            userMapper.updateRegisterStatus(applicant.getId(), "factory_rejected");
        }

        if (entity.getInviteCodeId() != null) {
            factoryInviteCodeMapper.markUsed(entity.getInviteCodeId(), applicant.getId());
        }

        return listFactoryAudits();
    }

    private UserEntity requireUser(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "用户不存在");
        }
        return user;
    }

    private void ensureCanApplyFactory(UserEntity user, List<String> roles) {
        if (roles.contains("demander")) {
            throw new BizException(ResultCode.CONFLICT, "当前账号已绑定租赁方，不能申请工厂入驻");
        }
        if (roles.contains("admin")) {
            throw new BizException(ResultCode.CONFLICT, "当前账号已是管理员，无需申请工厂入驻");
        }
        if (roles.contains("supplier")) {
            throw new BizException(ResultCode.CONFLICT, "当前账号已是工厂用户，无需重复申请");
        }
        String registerStatus = normalizeRegisterStatus(user.getRegisterStatus());
        if ("factory_pending".equals(registerStatus)) {
            throw new BizException(ResultCode.CONFLICT, "当前账号已有待审核的工厂入驻申请");
        }
    }

    private FactoryInviteCodeEntity requireInviteCode(String rawCode) {
        if (rawCode == null || rawCode.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "请输入管理员提供的邀请码");
        }
        FactoryInviteCodeEntity entity = factoryInviteCodeMapper.selectByCodeHash(hashInviteCode(rawCode.trim().toUpperCase(Locale.ROOT)));
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "邀请码不存在或已失效");
        }
        return entity;
    }

    private FactoryInviteCodeEntity requireLockedInviteCode(Long userId) {
        return factoryInviteCodeMapper.selectAll().stream()
            .filter(item -> "locked".equals(item.getStatus()) && userId.equals(item.getLockedByUserId()))
            .findFirst()
            .orElseThrow(() -> new BizException(ResultCode.CONFLICT, "请先输入并验证管理员提供的邀请码"));
    }

    private void validateInviteCodeAvailability(FactoryInviteCodeEntity entity, Long currentUserId) {
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "邀请码不存在或已失效");
        }
        if (entity.getExpireAt() != null && entity.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new BizException(ResultCode.CONFLICT, "邀请码已过期");
        }
        if ("revoked".equals(entity.getStatus())) {
            throw new BizException(ResultCode.CONFLICT, "邀请码已被管理员作废");
        }
        if ("used".equals(entity.getStatus())) {
            throw new BizException(ResultCode.CONFLICT, "邀请码已被使用");
        }
        if ("locked".equals(entity.getStatus()) && entity.getLockedByUserId() != null && !entity.getLockedByUserId().equals(currentUserId)) {
            throw new BizException(ResultCode.CONFLICT, "邀请码已被其他用户占用");
        }
    }

    private void ensureAdminHasSupplier(List<String> roles) {
        if (roles.contains("admin") && !roles.contains("supplier")) {
            throw new BizException(ResultCode.CONFLICT, "管理员账号缺少 supplier 角色绑定，请先修复账号角色数据");
        }
    }

    private String resolveCurrentRole(List<String> roles, String desiredRole) {
        if (desiredRole != null && !desiredRole.isBlank()) {
            String normalized = desiredRole.trim().toLowerCase(Locale.ROOT);
            if (!roles.contains(normalized)) {
                throw new BizException(ResultCode.FORBIDDEN, "当前微信账号未绑定所选角色，请先联系管理员配置");
            }
            return normalized;
        }
        if (roles.contains("admin")) {
            return "admin";
        }
        if (roles.contains("supplier")) {
            return "supplier";
        }
        if (roles.contains("demander")) {
            return "demander";
        }
        return "";
    }

    private String resolveDefaultEntry(List<String> roles, String registerStatus, FactoryAuditEntity latestAudit, String currentRole) {
        if ("supplier".equals(currentRole)) {
            return "factory_home";
        }
        if ("demander".equals(currentRole)) {
            return "demander_home";
        }
        if (roles.contains("admin")) {
            return "admin_home";
        }
        if (roles.contains("supplier")) {
            return "factory_home";
        }
        if (roles.contains("demander")) {
            return "demander_home";
        }
        if ("factory_pending".equals(registerStatus)) {
            return "factory_audit_status";
        }
        if ("factory_rejected".equals(registerStatus)) {
            return "factory_audit_status";
        }
        if (latestAudit != null && "pending".equals(latestAudit.getAuditStatus())) {
            return "factory_audit_status";
        }
        if (latestAudit != null && "rejected".equals(latestAudit.getAuditStatus())) {
            return "factory_audit_status";
        }
        return "role_select";
    }

    private String normalizeRegisterStatus(String registerStatus) {
        if (registerStatus == null || registerStatus.isBlank()) {
            return "new";
        }
        return registerStatus;
    }

    private Long requireAdminRole() {
        Long userId = currentUserSupport.requireCurrentUserId();
        List<String> roles = userRoleMapper.selectRoleCodesByUserId(userId);
        if (!roles.contains("admin")) {
            throw new BizException(ResultCode.FORBIDDEN, "当前账号不是管理员，无法执行该操作");
        }
        return userId;
    }

    private FactoryInviteVerifyResponse toFactoryInviteVerifyResponse(FactoryInviteCodeEntity entity) {
        FactoryInviteVerifyResponse response = new FactoryInviteVerifyResponse();
        response.setInviteCodeId(entity.getId());
        response.setCodeSuffix(entity.getCodeSuffix());
        response.setStatus(entity.getStatus());
        response.setExpireAt(TimeUtils.format(entity.getExpireAt()));
        return response;
    }

    private FactoryAuditStatusResponse toFactoryAuditStatusResponse(FactoryAuditEntity entity) {
        FactoryAuditStatusResponse response = new FactoryAuditStatusResponse();
        response.setAuditId(entity.getId());
        response.setStatus(entity.getAuditStatus());
        response.setStatusText(factoryAuditStatusText(entity.getAuditStatus()));
        response.setFactoryName(firstNotBlank(entity.getFactoryName(), entity.getCompanyName()));
        response.setContactName(entity.getContactName());
        response.setContactPhone(entity.getContactPhone());
        response.setSubmittedAt(TimeUtils.format(entity.getSubmittedAt()));
        response.setReviewedAt(TimeUtils.format(entity.getReviewedAt()));
        response.setRejectReason(firstNotBlank(entity.getRejectReason(), entity.getAuditRemark()));
        return response;
    }

    private FactoryAuditResponse toFactoryAuditResponse(FactoryAuditEntity entity) {
        FactoryAuditResponse response = new FactoryAuditResponse();
        response.setId(entity.getId());
        response.setCompany(firstNotBlank(entity.getFactoryName(), entity.getCompanyName()));
        response.setContact(entity.getContactName());
        response.setPhone(entity.getContactPhone());
        response.setStatus(entity.getAuditStatus());
        response.setStatusText(factoryAuditStatusText(entity.getAuditStatus()));
        response.setCreatedAt(TimeUtils.format(entity.getSubmittedAt()));
        response.setReviewedAt(TimeUtils.format(entity.getReviewedAt()));
        response.setRemark(firstNotBlank(entity.getRemark(), entity.getAuditRemark(), entity.getRejectReason()));
        return response;
    }

    private FactoryAuditDetailResponse toFactoryAuditDetailResponse(FactoryAuditEntity entity) {
        FactoryAuditDetailResponse response = new FactoryAuditDetailResponse();
        FactoryAuditResponse base = toFactoryAuditResponse(entity);
        response.setId(base.getId());
        response.setCompany(base.getCompany());
        response.setContact(base.getContact());
        response.setPhone(base.getPhone());
        response.setStatus(base.getStatus());
        response.setStatusText(base.getStatusText());
        response.setCreatedAt(base.getCreatedAt());
        response.setReviewedAt(base.getReviewedAt());
        response.setRemark(base.getRemark());
        response.setApplicantUserId(entity.getApplicantUserId());
        response.setInviteCodeId(entity.getInviteCodeId());
        response.setUnifiedSocialCreditCode(entity.getUnifiedSocialCreditCode());
        response.setBusinessLicenseUrl(entity.getBusinessLicenseUrl());
        response.setFactoryAddress(entity.getFactoryAddress());
        response.setMainBusiness(entity.getMainBusiness());
        response.setRejectReason(entity.getRejectReason());
        return response;
    }

    private FactoryInviteCodeResponse toFactoryInviteCodeResponse(FactoryInviteCodeEntity entity) {
        FactoryInviteCodeResponse response = new FactoryInviteCodeResponse();
        response.setId(entity.getId());
        response.setInviteCode(entity.getCodePlain());
        response.setCodeSuffix(entity.getCodeSuffix());
        response.setStatus(entity.getStatus());
        response.setStatusText(inviteStatusText(entity));
        response.setCreatedAt(TimeUtils.format(entity.getCreatedAt()));
        response.setExpireAt(TimeUtils.format(entity.getExpireAt()));
        response.setRemark(entity.getRemark());
        response.setLockedByUserId(entity.getLockedByUserId());
        response.setUsedByUserId(entity.getUsedByUserId());
        return response;
    }

    private String inviteStatusText(FactoryInviteCodeEntity entity) {
        if (entity.getExpireAt() != null && entity.getExpireAt().isBefore(LocalDateTime.now()) && "unused".equals(entity.getStatus())) {
            return "已过期";
        }
        return switch (entity.getStatus()) {
            case "locked" -> "已锁定";
            case "used" -> "已使用";
            case "revoked" -> "已作废";
            case "expired" -> "已过期";
            default -> "未使用";
        };
    }

    private String factoryAuditStatusText(String status) {
        return switch (status) {
            case "approved" -> "已通过";
            case "rejected" -> "已驳回";
            default -> "审核中";
        };
    }

    private String buildInviteCode() {
        return "MUHOU-" + randomAlphaNumeric(4) + "-" + randomAlphaNumeric(6);
    }

    private String randomAlphaNumeric(int length) {
        char[] source = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(source[RANDOM.nextInt(source.length)]);
        }
        return builder.toString();
    }

    private String hashInviteCode(String rawCode) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawCode.trim().toUpperCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to hash invite code", ex);
        }
    }

    private String firstNotBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    public static class LoginSnapshot {
        private String currentRole;
        private List<String> roleBindings;
        private String registerStatus;
        private String defaultEntry;
        private boolean needRoleSelection;
        private String factoryAuditStatus;
        private String factoryAuditRejectReason;

        public String getCurrentRole() { return currentRole; }
        public void setCurrentRole(String currentRole) { this.currentRole = currentRole; }
        public List<String> getRoleBindings() { return roleBindings; }
        public void setRoleBindings(List<String> roleBindings) { this.roleBindings = roleBindings; }
        public String getRegisterStatus() { return registerStatus; }
        public void setRegisterStatus(String registerStatus) { this.registerStatus = registerStatus; }
        public String getDefaultEntry() { return defaultEntry; }
        public void setDefaultEntry(String defaultEntry) { this.defaultEntry = defaultEntry; }
        public boolean isNeedRoleSelection() { return needRoleSelection; }
        public void setNeedRoleSelection(boolean needRoleSelection) { this.needRoleSelection = needRoleSelection; }
        public String getFactoryAuditStatus() { return factoryAuditStatus; }
        public void setFactoryAuditStatus(String factoryAuditStatus) { this.factoryAuditStatus = factoryAuditStatus; }
        public String getFactoryAuditRejectReason() { return factoryAuditRejectReason; }
        public void setFactoryAuditRejectReason(String factoryAuditRejectReason) { this.factoryAuditRejectReason = factoryAuditRejectReason; }
    }
}
