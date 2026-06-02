package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.common.support.StatusTextHelper;
import com.muhou.backend.common.util.MoneyUtils;
import com.muhou.backend.common.util.TimeUtils;
import com.muhou.backend.infrastructure.persistence.entity.AdminReviewEntity;
import com.muhou.backend.infrastructure.persistence.entity.DisputeEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderAdminActionEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropAuditEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropImageEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderEntity;
import com.muhou.backend.infrastructure.persistence.mapper.DisputeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderAdminActionMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderReviewMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropAuditMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropImageMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.web.response.AdminOverviewResponse;
import com.muhou.backend.web.response.AdminReviewResponse;
import com.muhou.backend.web.response.AdminUserResponse;
import com.muhou.backend.web.response.DisputeResponse;
import com.muhou.backend.web.response.FactoryAuditDetailResponse;
import com.muhou.backend.web.response.FactoryAuditResponse;
import com.muhou.backend.web.response.FactoryInviteCodeResponse;
import com.muhou.backend.web.response.FactoryInviteCreateResponse;
import com.muhou.backend.web.response.PropAuditResponse;
import com.muhou.backend.web.response.PropInstanceResponse;
import com.muhou.backend.web.response.PropInstanceStatusLogResponse;
import com.muhou.backend.web.response.PropResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminApplicationService {

    private final PropAuditMapper propAuditMapper;
    private final DisputeMapper disputeMapper;
    private final OrderReviewMapper orderReviewMapper;
    private final PropMapper propMapper;
    private final PropImageMapper propImageMapper;
    private final RentalOrderMapper rentalOrderMapper;
    private final UserApplicationService userApplicationService;
    private final CurrentUserSupport currentUserSupport;
    private final FactoryOnboardingApplicationService factoryOnboardingApplicationService;
    private final UserRoleMapper userRoleMapper;
    private final OrderSettlementApplicationService orderSettlementApplicationService;
    private final OrderAdminActionMapper orderAdminActionMapper;
    private final PropApplicationService propApplicationService;

    public AdminApplicationService(PropAuditMapper propAuditMapper,
                                   DisputeMapper disputeMapper,
                                   OrderReviewMapper orderReviewMapper,
                                   PropMapper propMapper,
                                   PropImageMapper propImageMapper,
                                   RentalOrderMapper rentalOrderMapper,
                                   UserApplicationService userApplicationService,
                                   CurrentUserSupport currentUserSupport,
                                   FactoryOnboardingApplicationService factoryOnboardingApplicationService,
                                   UserRoleMapper userRoleMapper,
                                   OrderSettlementApplicationService orderSettlementApplicationService,
                                   OrderAdminActionMapper orderAdminActionMapper,
                                   PropApplicationService propApplicationService) {
        this.propAuditMapper = propAuditMapper;
        this.disputeMapper = disputeMapper;
        this.orderReviewMapper = orderReviewMapper;
        this.propMapper = propMapper;
        this.propImageMapper = propImageMapper;
        this.rentalOrderMapper = rentalOrderMapper;
        this.userApplicationService = userApplicationService;
        this.currentUserSupport = currentUserSupport;
        this.factoryOnboardingApplicationService = factoryOnboardingApplicationService;
        this.userRoleMapper = userRoleMapper;
        this.orderSettlementApplicationService = orderSettlementApplicationService;
        this.orderAdminActionMapper = orderAdminActionMapper;
        this.propApplicationService = propApplicationService;
    }

    public AdminOverviewResponse getOverview() {
        requireAdminRole();
        List<FactoryAuditResponse> factoryAudits = listFactoryAudits();
        List<PropAuditResponse> propAudits = listPropAudits();
        List<DisputeResponse> disputes = listDisputes();
        List<AdminReviewResponse> reviews = listReviews();
        List<AdminUserResponse> users = userApplicationService.listAdminUsers();

        AdminOverviewResponse response = new AdminOverviewResponse();
        response.setPendingFactoryCount((int) factoryAudits.stream().filter(item -> "pending".equals(item.getStatus())).count());
        response.setPendingPropCount((int) propAudits.stream().filter(item -> "pending".equals(item.getStatus())).count());
        response.setPendingDisputeCount((int) disputes.stream().filter(item -> "pending".equals(item.getStatus())).count());
        response.setVisibleReviewCount((int) reviews.stream().filter(AdminReviewResponse::isVisible).count());
        response.setTotalUsers(users.size());
        return response;
    }

    public List<FactoryAuditResponse> listFactoryAudits() {
        return factoryOnboardingApplicationService.listFactoryAudits();
    }

    public List<FactoryAuditResponse> listFactoryAuditHistory() {
        return factoryOnboardingApplicationService.listFactoryAuditHistory();
    }

    public FactoryAuditDetailResponse getFactoryAuditDetail(Long id) {
        return factoryOnboardingApplicationService.getFactoryAuditDetail(id);
    }

    @Transactional
    public List<FactoryAuditResponse> reviewFactoryAudit(Long id, boolean approved, String remark) {
        List<FactoryAuditResponse> result = factoryOnboardingApplicationService.reviewFactoryAudit(id, approved, remark);
        recordGlobalAdminAction(
            approved ? "review_factory_audit_approved" : "review_factory_audit_rejected",
            "factory_audit",
            null,
            approved ? "管理员通过工厂入驻审核" : "管理员驳回工厂入驻审核",
            "factoryAuditId=" + id + "; remark=" + safe(remark)
        );
        return result;
    }

    public List<FactoryInviteCodeResponse> listFactoryInviteCodes() {
        return factoryOnboardingApplicationService.listInviteCodes();
    }

    @Transactional
    public FactoryInviteCreateResponse createFactoryInviteCode(Integer expireDays, String remark) {
        FactoryInviteCreateResponse response = factoryOnboardingApplicationService.createInviteCode(expireDays, remark);
        recordGlobalAdminAction(
            "generate_factory_invite_code",
            "factory_invite_code",
            null,
            "管理员生成工厂邀请码",
            "inviteCodeId=" + response.getId()
                + "; codeSuffix=" + safe(response.getCodeSuffix())
                + "; expireAt=" + safe(response.getExpireAt())
                + "; remark=" + safe(remark)
        );
        return response;
    }

    @Transactional
    public List<FactoryInviteCodeResponse> revokeFactoryInviteCode(Long id) {
        List<FactoryInviteCodeResponse> result = factoryOnboardingApplicationService.revokeInviteCode(id);
        recordGlobalAdminAction(
            "revoke_factory_invite_code",
            "factory_invite_code",
            null,
            "管理员作废工厂邀请码",
            "inviteCodeId=" + id
        );
        return result;
    }

    public List<PropAuditResponse> listPropAudits() {
        requireAdminRole();
        return propAuditMapper.selectPendingList().stream()
            .map(this::toPropAuditResponse)
            .collect(Collectors.toList());
    }

    public List<PropAuditResponse> listPropAuditHistory() {
        requireAdminRole();
        return propAuditMapper.selectHistoryList().stream()
            .map(this::toPropAuditResponse)
            .collect(Collectors.toList());
    }

    public PropAuditResponse getPropAuditDetail(Long id) {
        requireAdminRole();
        PropAuditEntity entity = propAuditMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到道具审核记录");
        }
        return toPropAuditResponse(entity);
    }

    @Transactional
    public List<PropAuditResponse> reviewPropAudit(Long id, boolean approved, String remark) {
        requireAdminRole();
        PropAuditEntity entity = propAuditMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到道具审核记录");
        }
        String reviewStatus = approved ? "approved" : "rejected";
        propAuditMapper.updateReview(id, reviewStatus, remark, userApplicationService.resolveAdminUserId());
        propMapper.updateAuditResult(
            entity.getPropId(),
            resolvePropAuditStatusAfterReview(entity.getActionType(), approved),
            resolvePropStatusAfterAudit(entity.getActionType(), approved)
        );
        recordGlobalAdminAction(
            approved ? "review_prop_audit_approved" : "review_prop_audit_rejected",
            "supplier",
            entity.getSupplierUserId(),
            approved ? "管理员通过道具审核" : "管理员驳回道具审核",
            "propAuditId=" + id
                + "; propId=" + entity.getPropId()
                + "; propAction=" + safe(entity.getActionType())
                + "; remark=" + safe(remark)
        );
        return listPropAudits();
    }

    public List<DisputeResponse> listDisputes() {
        requireAdminRole();
        return disputeMapper.selectAll().stream()
            .map(this::toDisputeResponse)
            .collect(Collectors.toList());
    }

    public DisputeResponse getDisputeDetail(Long id) {
        requireAdminRole();
        DisputeEntity entity = disputeMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到纠纷记录");
        }
        return toDisputeResponse(entity);
    }

    public List<DisputeResponse> listMyDisputes() {
        Long userId = currentUserSupport.requireCurrentUserId();
        String role = currentUserSupport.getCurrentRole();
        if (!List.of("demander", "supplier", "admin").contains(role)) {
            throw new BizException(ResultCode.FORBIDDEN, "当前角色无权查看仲裁记录");
        }
        return disputeMapper.selectByParticipantUserId(userId).stream()
            .map(this::toDisputeResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<DisputeResponse> decideDispute(Long id, String actionType, Integer decisionAmountFen, String resolution) {
        requireAdminRole();
        DisputeEntity entity = disputeMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "仲裁记录不存在");
        }
        if (!"pending".equals(entity.getDisputeStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前仲裁已处理，不能重复裁定");
        }
        RentalOrderEntity order = rentalOrderMapper.selectById(entity.getOrderId());
        if (order == null) {
            throw new BizException(ResultCode.NOT_FOUND, "订单不存在");
        }
        validateDisputeStage(entity);
        String normalizedAction = normalizeAdminActionType(entity.getApplyStage(), entity.getApplicantRole(), actionType);
        int amountFen = resolveDecisionAmount(order, normalizedAction, decisionAmountFen);
        boolean approved = !"reject".equals(normalizedAction);
        String finalResolution = resolution == null || resolution.isBlank() ? "管理员裁定：按平台规则处理" : resolution.trim();
        int updated = disputeMapper.decide(
            id,
            approved ? "resolved" : "closed",
            finalResolution,
            approved ? "approved" : "rejected",
            approved ? "refund_reserved" : "none",
            amountFen,
            approved ? "pending_settlement" : "none",
            normalizedAction,
            userApplicationService.resolveAdminUserId()
        );
        if (updated <= 0) {
            throw new BizException(ResultCode.CONFLICT, "当前仲裁状态已变化，请刷新后重试");
        }
        recordDisputeDecisionAction(order, entity, normalizedAction, amountFen, finalResolution);
        if (shouldRunFinalSettlement(entity)) {
            rentalOrderMapper.markReviewed(entity.getOrderId(), java.time.LocalDateTime.now());
            DisputeEntity decided = disputeMapper.selectById(id);
            List<DisputeEntity> disputes = disputeMapper.selectByOrderId(entity.getOrderId()).stream()
                .map(item -> item.getId().equals(id) ? decided : item)
                .toList();
            orderSettlementApplicationService.settleFinalOrder(order, disputes);
        }
        return listDisputes();
    }

    public List<AdminReviewResponse> listReviews() {
        requireAdminRole();
        return orderReviewMapper.selectAdminList().stream()
            .map(this::toAdminReviewResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<AdminReviewResponse> hideReview(Long id) {
        requireAdminRole();
        orderReviewMapper.updateVisibleFlag(id, 0);
        return listReviews();
    }

    @Transactional
    public List<AdminReviewResponse> deleteReview(Long id) {
        requireAdminRole();
        orderReviewMapper.deleteById(id);
        return listReviews();
    }

    public List<AdminUserResponse> listUsers() {
        requireAdminRole();
        return userApplicationService.listAdminUsers();
    }

    @Transactional
    public List<AdminUserResponse> bindUserRole(Long userId, String role) {
        requireAdminRole();
        userApplicationService.bindRoleToUser(userId, role);
        recordGlobalAdminAction(
            "bind_user_role",
            "user",
            userId,
            "管理员绑定用户角色",
            "role=" + safe(role)
        );
        return userApplicationService.listAdminUsers();
    }

    @Transactional
    public List<AdminUserResponse> unbindUserRole(Long userId, String role) {
        requireAdminRole();
        userApplicationService.unbindRoleFromUser(userId, role);
        recordGlobalAdminAction(
            "unbind_user_role",
            "user",
            userId,
            "管理员解绑用户角色",
            "role=" + safe(role)
        );
        return userApplicationService.listAdminUsers();
    }

    @Transactional
    public PropResponse createPendingFillProp() {
        requireAdminRole();
        PropResponse response = propApplicationService.createPendingFillProp();
        recordGlobalAdminAction(
            "generate_prop_qr_code",
            "prop_qr_code",
            null,
            "管理员生成未登记道具二维码",
            "propId=" + response.getId() + "; qrCodeId=" + safe(response.getQrCodeId())
        );
        return response;
    }

    public List<PropResponse> listAdminPendingFillProps() {
        requireAdminRole();
        return propApplicationService.listAdminPendingFillProps();
    }

    public List<PropResponse> listAdminQrCodeProps() {
        requireAdminRole();
        return propApplicationService.listAdminQrCodeProps();
    }

    @Transactional
    public List<PropResponse> revokeQrCode(Long propId) {
        requireAdminRole();
        List<PropResponse> result = propApplicationService.revokePendingQrCode(propId);
        recordGlobalAdminAction(
            "revoke_prop_qr_code",
            "prop_qr_code",
            null,
            "管理员作废未登记道具二维码",
            "propId=" + propId
        );
        return result;
    }

    public List<PropInstanceResponse> listPropInstances(Long propId) {
        requireAdminRole();
        return propApplicationService.listAdminPropInstances(propId);
    }

    @Transactional
    public PropInstanceResponse updatePropInstanceStatus(Long instanceId, String targetStatus, String reason, Long relatedOrderId) {
        requireAdminRole();
        return propApplicationService.updateAdminInstanceStatus(instanceId, targetStatus, reason, relatedOrderId);
    }

    public List<PropInstanceStatusLogResponse> listPropInstanceStatusLogs(Long instanceId) {
        requireAdminRole();
        return propApplicationService.listAdminInstanceStatusLogs(instanceId);
    }

    private PropAuditResponse toPropAuditResponse(PropAuditEntity entity) {
        PropAuditResponse response = new PropAuditResponse();
        response.setId(entity.getId());
        response.setPropId(entity.getPropId());
        response.setPropName(entity.getPropName());
        response.setAction(entity.getActionType());
        response.setActionText(propActionText(entity.getActionType()));
        response.setStatus(entity.getAuditStatus());
        response.setStatusText(auditStatusText(entity.getAuditStatus()));
        response.setCreatedAt(TimeUtils.format(entity.getSubmittedAt()));
        response.setReviewedAt(TimeUtils.format(entity.getReviewedAt()));
        response.setRemark(entity.getAuditRemark() == null || entity.getAuditRemark().isBlank() ? entity.getApplyRemark() : entity.getAuditRemark());
        List<String> images = loadPropImages(entity.getPropId(), entity.getImageUrl());
        response.setImageUrl(images.get(0));
        response.setImages(images);
        response.setStyle(entity.getStyleCode());
        response.setType(entity.getTypeCode());
        response.setSize(entity.getSizeDesc());
        response.setLengthCm(entity.getLengthCm());
        response.setWidthCm(entity.getWidthCm());
        response.setHeightCm(entity.getHeightCm());
        response.setMaterial(entity.getMaterialDesc());
        response.setPrice(MoneyUtils.fenToYuan(entity.getDailyRentPriceFen()).toPlainString());
        response.setDeposit(MoneyUtils.fenToYuan(entity.getDepositAmountFen()).toPlainString());
        response.setFireResistantOption(entity.getFireResistantOption());
        response.setWeight(entity.getWeightDesc());
        response.setTransportSuggestion(entity.getTransportSuggestion());
        response.setPropStatus(entity.getPropStatus());
        response.setPropStatusText(resolveAuditViewPropStatusText(entity));
        response.setPropAuditStatus(entity.getPropAuditStatus());
        response.setPropAuditStatusText(StatusTextHelper.auditStatusText(entity.getPropAuditStatus()));
        response.setFillStatus(entity.getFillStatus());
        response.setQrCodeId(entity.getQrCodeId());
        response.setSupplierUserId(entity.getSupplierUserId());
        response.setSupplierName(entity.getSupplierNickname());
        response.setSupplierPhone(entity.getSupplierPhone());
        return response;
    }

    private DisputeResponse toDisputeResponse(DisputeEntity entity) {
        DisputeResponse response = new DisputeResponse();
        response.setId(entity.getId());
        response.setOrderId(entity.getOrderId());
        response.setOrderNo(entity.getOrderNo());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setApplicant(entity.getApplicantName());
        response.setApplicantRole(entity.getApplicantRole());
        response.setApplicantRoleText("supplier".equals(entity.getApplicantRole()) ? "工厂方" : "租赁方");
        response.setApplyStage(entity.getApplyStage());
        response.setApplyStageText(disputeStageText(entity.getApplyStage()));
        response.setReasonCode(entity.getReasonCode());
        response.setReasonLabel(entity.getReasonLabel());
        response.setClaimAmount(MoneyUtils.fenToYuan(entity.getClaimAmountFen()));
        response.setAdminDecisionAmount(MoneyUtils.fenToYuan(entity.getAdminDecisionAmountFen()));
        response.setDepositAmount(MoneyUtils.fenToYuan(entity.getDepositAmountFenSnapshot()));
        response.setFundEffectStatus(entity.getFundEffectStatus());
        response.setAdminActionType(entity.getAdminActionType());
        response.setEvidenceUrls(entity.getEvidenceUrls());
        response.setEvidenceImages(parseImages(entity.getEvidenceUrls()).stream()
            .filter(item -> !"/images/stage-prop-real.jpg".equals(item))
            .toList());
        response.setStatus(entity.getDisputeStatus());
        response.setStatusText(disputeStatusText(entity.getDisputeStatus()));
        response.setCreatedAt(TimeUtils.format(entity.getCreatedAt()));
        response.setResolvedAt(TimeUtils.format(entity.getResolvedAt()));
        response.setResolution(entity.getResolution());
        response.setResolutionType(entity.getResolutionType());
        response.setResolutionTypeText(resolutionTypeText(entity.getResolutionType()));
        response.setRefundStatus(entity.getRefundStatus());
        return response;
    }

    private AdminReviewResponse toAdminReviewResponse(AdminReviewEntity entity) {
        AdminReviewResponse response = new AdminReviewResponse();
        response.setId(entity.getId());
        response.setOrderId(entity.getOrderId());
        response.setOrderNo(entity.getOrderNo());
        response.setUser(entity.getReviewerName());
        response.setReviewerRole(entity.getReviewerRole());
        response.setScore(entity.getScore());
        response.setPropScore(entity.getPropScore());
        response.setCounterpartyScore(entity.getCounterpartyScore());
        response.setContent(entity.getContent());
        response.setVisible(entity.getVisibleFlag() != null && entity.getVisibleFlag() == 1);
        response.setAuto(entity.getAutoFlag() != null && entity.getAutoFlag() == 1);
        response.setVisibleText(response.isVisible() ? "显示中" : "已隐藏");
        response.setCreatedAt(TimeUtils.format(entity.getCreatedAt()));
        return response;
    }

    private String auditStatusText(String status) {
        if ("approved".equals(status)) {
            return "已通过";
        }
        if ("rejected".equals(status)) {
            return "已驳回";
        }
        return "待审核";
    }

    private String disputeStatusText(String status) {
        if ("resolved".equals(status)) {
            return "已同意";
        }
        if ("closed".equals(status)) {
            return "已拒绝";
        }
        return "待仲裁";
    }

    private String resolutionTypeText(String type) {
        if ("approved".equals(type)) {
            return "同意仲裁";
        }
        if ("rejected".equals(type)) {
            return "拒绝仲裁";
        }
        return "";
    }

    private String disputeStageText(String stage) {
        if ("wait_pickup".equals(stage)) {
            return "待取货";
        }
        if ("renting".equals(stage)) {
            return "租赁中";
        }
        if ("wait_review".equals(stage)) {
            return "待评价";
        }
        return "未知节点";
    }

    private String propActionText(String action) {
        if ("down".equals(action)) {
            return "下架";
        }
        if ("create".equals(action)) {
            return "新增";
        }
        return "上架";
    }

    private void validateDisputeStage(DisputeEntity dispute) {
        if ("renting".equals(dispute.getApplyStage()) && "demander".equals(dispute.getApplicantRole())) {
            return;
        }
        if ("wait_review".equals(dispute.getApplyStage()) && "supplier".equals(dispute.getApplicantRole())) {
            return;
        }
        throw new BizException(ResultCode.CONFLICT, "当前订单阶段和发起方不允许处理正式仲裁");
    }

    private String normalizeAdminActionType(String applyStage, String applicantRole, String requestedActionType) {
        if ("reject".equals(requestedActionType)) {
            return "reject";
        }
        if ("renting".equals(applyStage) && "demander".equals(applicantRole)
            && "approve_demander_compensation".equals(requestedActionType)) {
            return "approve_demander_compensation";
        }
        if ("wait_review".equals(applyStage) && "supplier".equals(applicantRole)
            && "approve_supplier_compensation".equals(requestedActionType)) {
            return "approve_supplier_compensation";
        }
        throw new BizException(ResultCode.VALIDATION_ERROR, "当前订单阶段不支持该仲裁裁定动作");
    }

    private boolean shouldRunFinalSettlement(DisputeEntity dispute) {
        return "wait_review".equals(dispute.getApplyStage()) && "supplier".equals(dispute.getApplicantRole());
    }

    private int resolveDecisionAmount(RentalOrderEntity order, String actionType, Integer decisionAmountFen) {
        if ("reject".equals(actionType)) {
            return 0;
        }
        if (decisionAmountFen == null) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "裁定金额不能为空");
        }
        if (decisionAmountFen <= 0) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "同意补偿时裁定金额必须大于 0");
        }
        int amountFen = decisionAmountFen;
        if ("approve_demander_compensation".equals(actionType) && amountFen > positive(order.getRentAmountFen())) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "用户补偿金额不能超过订单租金");
        }
        if ("approve_supplier_compensation".equals(actionType) && amountFen > positive(order.getDepositAmountFen())) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "工厂补偿金额不能超过订单押金");
        }
        return amountFen;
    }

    private int positive(Integer value) {
        return value == null || value < 0 ? 0 : value;
    }

    private void recordDisputeDecisionAction(RentalOrderEntity order,
                                             DisputeEntity dispute,
                                             String actionType,
                                             Integer amountFen,
                                             String reason) {
        OrderAdminActionEntity action = new OrderAdminActionEntity();
        action.setOrderId(order.getId());
        action.setDisputeId(dispute.getId());
        action.setActionType("dispute_decision");
        action.setActionStatus("done");
        action.setBeforeOrderStatus(order.getOrderStatus());
        action.setAfterOrderStatus(shouldRunFinalSettlement(dispute) ? "completed" : order.getOrderStatus());
        action.setBeforePayStatus(order.getPayStatus());
        action.setAfterPayStatus(order.getPayStatus());
        action.setAmountFen(amountFen);
        action.setTargetRole(dispute.getApplicantRole());
        action.setTargetUserId(dispute.getApplyUserId());
        action.setScoreDelta(0);
        action.setReason(reason);
        action.setInternalNote(actionType);
        action.setOperatorUserId(userApplicationService.resolveAdminUserId());
        orderAdminActionMapper.insert(action);
    }

    private void recordGlobalAdminAction(String actionType,
                                         String targetRole,
                                         Long targetUserId,
                                         String reason,
                                         String internalNote) {
        OrderAdminActionEntity action = new OrderAdminActionEntity();
        action.setOrderId(null);
        action.setDisputeId(null);
        action.setActionType(actionType);
        action.setActionStatus("done");
        action.setTargetRole(targetRole);
        action.setTargetUserId(targetUserId);
        action.setScoreDelta(0);
        action.setReason(reason);
        action.setInternalNote(internalNote);
        action.setOperatorUserId(userApplicationService.resolveAdminUserId());
        orderAdminActionMapper.insert(action);
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("\n", " ").replace("\r", " ").trim();
    }

    private void requireAdminRole() {
        String currentRole = currentUserSupport.getCurrentRole();
        Long currentUserId = currentUserSupport.requireCurrentUserId();
        if (!"admin".equals(currentRole) && !userRoleMapper.selectRoleCodesByUserId(currentUserId).contains("admin")) {
            throw new BizException(ResultCode.FORBIDDEN, "当前账号不是管理员，无法执行该操作");
        }
    }

    private List<String> parseImages(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return List.of("/images/stage-prop-real.jpg");
        }
        return Arrays.stream(imageUrl.split("[,;\\n]"))
            .map(String::trim)
            .filter(item -> !item.isBlank())
            .distinct()
            .toList();
    }

    private List<String> loadPropImages(Long propId, String fallbackImageUrl) {
        if (propId != null) {
            List<String> images = propImageMapper.selectByPropId(propId).stream()
                .map(PropImageEntity::getImageUrl)
                .filter(item -> item != null && !item.isBlank())
                .toList();
            if (!images.isEmpty()) {
                return images;
            }
        }
        return parseImages(fallbackImageUrl);
    }

    private String resolvePropStatusAfterAudit(String action, boolean approved) {
        if (!approved) {
            return "down".equals(action) ? "idle" : "offline";
        }
        if ("down".equals(action)) {
            return "offline";
        }
        return "idle";
    }

    private String resolvePropAuditStatusAfterReview(String action, boolean approved) {
        if (approved) {
            return "approved";
        }
        return ("up".equals(action) || "down".equals(action)) ? "approved" : "rejected";
    }

    private String resolveAuditViewPropStatusText(PropAuditEntity entity) {
        if ("pending".equals(entity.getPropAuditStatus())) {
            return "待审核";
        }
        if ("rejected".equals(entity.getPropAuditStatus())) {
            return "已驳回";
        }
        return StatusTextHelper.propStatusText(entity.getPropStatus());
    }
}
