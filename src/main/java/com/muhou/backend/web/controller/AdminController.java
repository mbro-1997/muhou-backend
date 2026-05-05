package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.AdminApplicationService;
import com.muhou.backend.application.service.PropApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.AdminAuditDecisionRequest;
import com.muhou.backend.web.request.AdminBindRoleRequest;
import com.muhou.backend.web.request.AdminDisputeResolveRequest;
import com.muhou.backend.web.request.FactoryInviteCreateRequest;
import com.muhou.backend.web.response.AdminOverviewResponse;
import com.muhou.backend.web.response.AdminReviewResponse;
import com.muhou.backend.web.response.AdminUserResponse;
import com.muhou.backend.web.response.DisputeResponse;
import com.muhou.backend.web.response.FactoryAuditDetailResponse;
import com.muhou.backend.web.response.FactoryAuditResponse;
import com.muhou.backend.web.response.FactoryInviteCodeResponse;
import com.muhou.backend.web.response.FactoryInviteCreateResponse;
import com.muhou.backend.web.response.PropResponse;
import com.muhou.backend.web.response.PropAuditResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminApplicationService adminApplicationService;
    private final PropApplicationService propApplicationService;

    public AdminController(AdminApplicationService adminApplicationService,
                           PropApplicationService propApplicationService) {
        this.adminApplicationService = adminApplicationService;
        this.propApplicationService = propApplicationService;
    }

    @GetMapping("/overview")
    public ApiResponse<AdminOverviewResponse> overview() {
        return ApiResponse.success(adminApplicationService.getOverview());
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

    @PostMapping("/disputes/{id}/resolve")
    public ApiResponse<List<DisputeResponse>> resolveDispute(@PathVariable Long id,
                                                             @RequestBody(required = false) AdminDisputeResolveRequest request) {
        String resolution = request == null ? null : request.getResolution();
        return ApiResponse.success(adminApplicationService.resolveDispute(id, resolution));
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
        return ApiResponse.success(propApplicationService.createPendingFillProp());
    }

    @GetMapping("/props/pending-fill")
    public ApiResponse<List<PropResponse>> pendingFillProps() {
        return ApiResponse.success(propApplicationService.listAdminPendingFillProps());
    }

    @GetMapping("/props/qr-codes")
    public ApiResponse<List<PropResponse>> qrCodeProps() {
        return ApiResponse.success(propApplicationService.listAdminQrCodeProps());
    }

    @PostMapping("/props/qr-codes/{propId}/revoke")
    public ApiResponse<List<PropResponse>> revokeQrCode(@PathVariable Long propId) {
        return ApiResponse.success(propApplicationService.revokePendingQrCode(propId));
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
