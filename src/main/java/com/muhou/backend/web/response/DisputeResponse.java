package com.muhou.backend.web.response;

import java.math.BigDecimal;
import java.util.List;

public class DisputeResponse {

    private Long id;
    private Long orderId;
    private String orderNo;
    private String title;
    private String content;
    private String applicant;
    private String applicantRole;
    private String applicantRoleText;
    private String applyStage;
    private String applyStageText;
    private String reasonCode;
    private String reasonLabel;
    private BigDecimal claimAmount;
    private BigDecimal adminDecisionAmount;
    private BigDecimal depositAmount;
    private String evidenceUrls;
    private List<String> evidenceImages;
    private String status;
    private String statusText;
    private String createdAt;
    private String resolvedAt;
    private String resolution;
    private String resolutionType;
    private String resolutionTypeText;
    private String refundStatus;
    private String fundEffectStatus;
    private String adminActionType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getApplicantRole() { return applicantRole; }
    public void setApplicantRole(String applicantRole) { this.applicantRole = applicantRole; }
    public String getApplicantRoleText() { return applicantRoleText; }
    public void setApplicantRoleText(String applicantRoleText) { this.applicantRoleText = applicantRoleText; }
    public String getApplyStage() { return applyStage; }
    public void setApplyStage(String applyStage) { this.applyStage = applyStage; }
    public String getApplyStageText() { return applyStageText; }
    public void setApplyStageText(String applyStageText) { this.applyStageText = applyStageText; }
    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
    public String getReasonLabel() { return reasonLabel; }
    public void setReasonLabel(String reasonLabel) { this.reasonLabel = reasonLabel; }
    public BigDecimal getClaimAmount() { return claimAmount; }
    public void setClaimAmount(BigDecimal claimAmount) { this.claimAmount = claimAmount; }
    public BigDecimal getAdminDecisionAmount() { return adminDecisionAmount; }
    public void setAdminDecisionAmount(BigDecimal adminDecisionAmount) { this.adminDecisionAmount = adminDecisionAmount; }
    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    public String getEvidenceUrls() { return evidenceUrls; }
    public void setEvidenceUrls(String evidenceUrls) { this.evidenceUrls = evidenceUrls; }
    public List<String> getEvidenceImages() { return evidenceImages; }
    public void setEvidenceImages(List<String> evidenceImages) { this.evidenceImages = evidenceImages; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(String resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getResolutionType() { return resolutionType; }
    public void setResolutionType(String resolutionType) { this.resolutionType = resolutionType; }
    public String getResolutionTypeText() { return resolutionTypeText; }
    public void setResolutionTypeText(String resolutionTypeText) { this.resolutionTypeText = resolutionTypeText; }
    public String getRefundStatus() { return refundStatus; }
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }
    public String getFundEffectStatus() { return fundEffectStatus; }
    public void setFundEffectStatus(String fundEffectStatus) { this.fundEffectStatus = fundEffectStatus; }
    public String getAdminActionType() { return adminActionType; }
    public void setAdminActionType(String adminActionType) { this.adminActionType = adminActionType; }
}
