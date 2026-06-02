package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.AdminApplicationService;
import com.muhou.backend.application.service.AdminConsoleApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.AdminAuditDecisionRequest;
import com.muhou.backend.web.request.AdminBindRoleRequest;
import com.muhou.backend.web.request.AdminDisputeDecisionRequest;
import com.muhou.backend.web.request.AdminOrderActionRequest;
import com.muhou.backend.web.request.FactoryInviteCreateRequest;
import com.muhou.backend.web.request.PropInstanceStatusUpdateRequest;
import com.muhou.backend.web.response.AdminOverviewResponse;
import com.muhou.backend.web.response.AdminReviewResponse;
import com.muhou.backend.web.response.AdminUserResponse;
import com.muhou.backend.web.response.DisputeResponse;
import com.muhou.backend.web.response.FactoryAuditDetailResponse;
import com.muhou.backend.web.response.FactoryAuditResponse;
import com.muhou.backend.web.response.FactoryInviteCodeResponse;
import com.muhou.backend.web.response.FactoryInviteCreateResponse;
import com.muhou.backend.web.response.PropInstanceResponse;
import com.muhou.backend.web.response.PropInstanceStatusLogResponse;
import com.muhou.backend.web.response.PropResponse;
import com.muhou.backend.web.response.PropAuditResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminApplicationService adminApplicationService;
    private final AdminConsoleApplicationService adminConsoleApplicationService;

    public AdminController(AdminApplicationService adminApplicationService,
                           AdminConsoleApplicationService adminConsoleApplicationService) {
        this.adminApplicationService = adminApplicationService;
        this.adminConsoleApplicationService = adminConsoleApplicationService;
    }

    @GetMapping("/overview")
    public ApiResponse<AdminOverviewResponse> overview() {
        return ApiResponse.success(adminApplicationService.getOverview());
    }

    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> dashboard() {
        return ApiResponse.success(adminConsoleApplicationService.dashboard());
    }

    @GetMapping("/orders")
    public ApiResponse<List<Map<String, Object>>> adminOrders(@RequestParam(required = false) String orderNo,
                                                              @RequestParam(required = false) String demanderKeyword,
                                                              @RequestParam(required = false) String supplierKeyword,
                                                              @RequestParam(required = false) String orderStatus,
                                                              @RequestParam(required = false) String payStatus,
                                                              @RequestParam(required = false) String refundStatus,
                                                              @RequestParam(required = false) Boolean hasDispute,
                                                              @RequestParam(required = false) Boolean hasPendingDispute,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "20") int pageSize) {
        return ApiResponse.success(adminConsoleApplicationService.listOrders(orderNo, demanderKeyword, supplierKeyword,
            orderStatus, payStatus, refundStatus, hasDispute, hasPendingDispute, page, pageSize));
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<Map<String, Object>> adminOrderDetail(@PathVariable Long orderId) {
        return ApiResponse.success(adminConsoleApplicationService.getOrderDetail(orderId));
    }

    @GetMapping("/orders/{orderId}/actions")
    public ApiResponse<?> adminOrderActions(@PathVariable Long orderId) {
        return ApiResponse.success(adminConsoleApplicationService.listOrderActions(orderId));
    }

    @PostMapping("/orders/{orderId}/actions")
    public ApiResponse<?> addAdminOrderAction(@PathVariable Long orderId,
                                              @RequestBody AdminOrderActionRequest request) {
        return ApiResponse.success(adminConsoleApplicationService.addOrderAction(orderId, request));
    }

    @GetMapping("/fund-flows")
    public ApiResponse<List<Map<String, Object>>> fundFlows(@RequestParam(required = false) String orderNo,
                                                            @RequestParam(required = false) String flowType,
                                                            @RequestParam(required = false) String receiverRole,
                                                            @RequestParam(required = false) String channelStatus,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "20") int pageSize) {
        return ApiResponse.success(adminConsoleApplicationService.listFundFlows(orderNo, flowType, receiverRole, channelStatus, page, pageSize));
    }

    @GetMapping("/prop-instances")
    public ApiResponse<List<Map<String, Object>>> propInstances(@RequestParam(required = false) String keyword,
                                                                @RequestParam(required = false) Long supplierId,
                                                                @RequestParam(required = false) Long propId,
                                                                @RequestParam(required = false) String instanceStatus,
                                                                @RequestParam(required = false) Long currentOrderId,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "20") int pageSize) {
        return ApiResponse.success(adminConsoleApplicationService.listPropInstances(keyword, supplierId, propId, instanceStatus, currentOrderId, page, pageSize));
    }

    @GetMapping("/actions")
    public ApiResponse<?> actions(@RequestParam(required = false) String orderNo,
                                  @RequestParam(required = false) String actionType,
                                  @RequestParam(required = false) Long operatorUserId,
                                  @RequestParam(required = false) String targetRole,
                                  @RequestParam(required = false) Long targetUserId,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int pageSize) {
        return ApiResponse.success(adminConsoleApplicationService.listActions(orderNo, actionType, operatorUserId, targetRole, targetUserId, page, pageSize));
    }

    @GetMapping("/dispute-reasons")
    public ApiResponse<?> adminDisputeReasons() {
        return ApiResponse.success(adminConsoleApplicationService.listDisputeReasons(null, null));
    }

    @GetMapping("/factory-audits")
    public ApiResponse<List<FactoryAuditResponse>> factoryAudits() {
        return ApiResponse.success(adminApplicationService.listFactoryAudits());
    }

    @GetMapping("/factory-audits/history")
    public ApiResponse<List<FactoryAuditResponse>> factoryAuditHistory() {
        return ApiResponse.success(adminApplicationService.listFactoryAuditHistory());
    }

    @GetMapping("/factory-audits/{id}")
    public ApiResponse<FactoryAuditDetailResponse> factoryAuditDetail(@PathVariable Long id) {
        return ApiResponse.success(adminApplicationService.getFactoryAuditDetail(id));
    }

    @PostMapping("/factory-audits/{id}/review")
    public ApiResponse<List<FactoryAuditResponse>> reviewFactoryAudit(@PathVariable Long id,
                                                                      @RequestBody(required = false) AdminAuditDecisionRequest request) {
        boolean approved = request != null && Boolean.TRUE.equals(request.getApproved());
        String remark = request == null ? null : request.getRemark();
        return ApiResponse.success(adminApplicationService.reviewFactoryAudit(id, approved, remark));
    }

    @GetMapping("/factory-invite-codes")
    public ApiResponse<List<FactoryInviteCodeResponse>> factoryInviteCodes() {
        return ApiResponse.success(adminApplicationService.listFactoryInviteCodes());
    }

    @PostMapping("/factory-invite-codes")
    public ApiResponse<FactoryInviteCreateResponse> createFactoryInviteCode(@RequestBody(required = false) FactoryInviteCreateRequest request) {
        Integer expireDays = request == null ? null : request.getExpireDays();
        String remark = request == null ? null : request.getRemark();
        return ApiResponse.success(adminApplicationService.createFactoryInviteCode(expireDays, remark));
    }

    @PostMapping("/factory-invite-codes/{id}/revoke")
    public ApiResponse<List<FactoryInviteCodeResponse>> revokeFactoryInviteCode(@PathVariable Long id) {
        return ApiResponse.success(adminApplicationService.revokeFactoryInviteCode(id));
    }

    @GetMapping("/prop-audits")
    public ApiResponse<List<PropAuditResponse>> propAudits() {
        return ApiResponse.success(adminApplicationService.listPropAudits());
    }

    @GetMapping("/prop-audits/history")
    public ApiResponse<List<PropAuditResponse>> propAuditHistory() {
        return ApiResponse.success(adminApplicationService.listPropAuditHistory());
    }

    @GetMapping("/prop-audits/{id}")
    public ApiResponse<PropAuditResponse> propAuditDetail(@PathVariable Long id) {
        return ApiResponse.success(adminApplicationService.getPropAuditDetail(id));
    }

    @PostMapping("/prop-audits/{id}/review")
    public ApiResponse<List<PropAuditResponse>> reviewPropAudit(@PathVariable Long id,
                                                                @RequestBody(required = false) AdminAuditDecisionRequest request) {
        boolean approved = request != null && Boolean.TRUE.equals(request.getApproved());
        String remark = request == null ? null : request.getRemark();
        return ApiResponse.success(adminApplicationService.reviewPropAudit(id, approved, remark));
    }

    @GetMapping("/disputes")
    public ApiResponse<List<DisputeResponse>> disputes() {
        return ApiResponse.success(adminApplicationService.listDisputes());
    }

    @GetMapping("/disputes/{id}")
    public ApiResponse<DisputeResponse> disputeDetail(@PathVariable Long id) {
        return ApiResponse.success(adminApplicationService.getDisputeDetail(id));
    }

    @PostMapping("/disputes/{id}/decision")
    public ApiResponse<List<DisputeResponse>> decideDispute(@PathVariable Long id,
                                                            @Valid @RequestBody AdminDisputeDecisionRequest request) {
        return ApiResponse.success(adminApplicationService.decideDispute(
            id,
            request.getAdminActionType(),
            request.getDecisionAmount() == null ? null : request.getDecisionAmount().movePointRight(2).intValue(),
            request.getReason()
        ));
    }

    @GetMapping("/reviews")
    public ApiResponse<List<AdminReviewResponse>> reviews() {
        return ApiResponse.success(adminApplicationService.listReviews());
    }

    @PostMapping("/reviews/{id}/hide")
    public ApiResponse<List<AdminReviewResponse>> hideReview(@PathVariable Long id) {
        return ApiResponse.success(adminApplicationService.hideReview(id));
    }

    @DeleteMapping("/reviews/{id}")
    public ApiResponse<List<AdminReviewResponse>> deleteReview(@PathVariable Long id) {
        return ApiResponse.success(adminApplicationService.deleteReview(id));
    }

    @GetMapping("/users")
    public ApiResponse<List<AdminUserResponse>> users() {
        return ApiResponse.success(adminApplicationService.listUsers());
    }

    @PostMapping("/props/qr-code")
    public ApiResponse<PropResponse> generatePropQrCode() {
        return ApiResponse.success(adminApplicationService.createPendingFillProp());
    }

    @GetMapping("/props/pending-fill")
    public ApiResponse<List<PropResponse>> pendingFillProps() {
        return ApiResponse.success(adminApplicationService.listAdminPendingFillProps());
    }

    @GetMapping("/props/qr-codes")
    public ApiResponse<List<PropResponse>> qrCodeProps() {
        return ApiResponse.success(adminApplicationService.listAdminQrCodeProps());
    }

    @PostMapping("/props/qr-codes/{propId}/revoke")
    public ApiResponse<List<PropResponse>> revokeQrCode(@PathVariable Long propId) {
        return ApiResponse.success(adminApplicationService.revokeQrCode(propId));
    }

    @GetMapping("/props/{propId}/instances")
    public ApiResponse<List<PropInstanceResponse>> adminPropInstances(@PathVariable Long propId) {
        return ApiResponse.success(adminApplicationService.listPropInstances(propId));
    }

    @PostMapping("/prop-instances/{instanceId}/status")
    public ApiResponse<PropInstanceResponse> updateAdminPropInstanceStatus(@PathVariable Long instanceId,
                                                                           @Valid @RequestBody PropInstanceStatusUpdateRequest request) {
        return ApiResponse.success(adminApplicationService.updatePropInstanceStatus(instanceId, request.getTargetStatus(), request.getReason(), request.getRelatedOrderId()));
    }

    @GetMapping("/prop-instances/{instanceId}/status-logs")
    public ApiResponse<List<PropInstanceStatusLogResponse>> adminPropInstanceStatusLogs(@PathVariable Long instanceId) {
        return ApiResponse.success(adminApplicationService.listPropInstanceStatusLogs(instanceId));
    }

    @PostMapping("/users/{id}/roles")
    public ApiResponse<List<AdminUserResponse>> bindUserRole(@PathVariable Long id,
                                                             @Valid @RequestBody AdminBindRoleRequest request) {
        return ApiResponse.success(adminApplicationService.bindUserRole(id, request.getRole()));
    }

    @DeleteMapping("/users/{id}/roles/{role}")
    public ApiResponse<List<AdminUserResponse>> unbindUserRole(@PathVariable Long id,
                                                               @PathVariable String role) {
        return ApiResponse.success(adminApplicationService.unbindUserRole(id, role));
    }
}
