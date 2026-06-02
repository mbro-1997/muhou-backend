package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class DisputeEntity {

    private Long id;
    private Long orderId;
    private Long applyUserId;
    private String applicantRole;
    private String applyStage;
    private String reasonCode;
    private String reasonLabel;
    private String title;
    private String content;
    private Integer claimAmountFen;
    private Integer adminDecisionAmountFen;
    private Integer depositAmountFenSnapshot;
    private String orderStatusSnapshot;
    private Long demanderUserId;
    private Long supplierUserId;
    private String evidenceUrls;
    private String disputeStatus;
    private String resolution;
    private String resolutionType;
    private String refundStatus;
    private String fundEffectStatus;
    private String adminActionType;
    private Long reviewerId;
    private String applicantName;
    private String orderNo;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getApplyUserId() { return applyUserId; }
    public void setApplyUserId(Long applyUserId) { this.applyUserId = applyUserId; }
    public String getApplicantRole() { return applicantRole; }
    public void setApplicantRole(String applicantRole) { this.applicantRole = applicantRole; }
    public String getApplyStage() { return applyStage; }
    public void setApplyStage(String applyStage) { this.applyStage = applyStage; }
    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
    public String getReasonLabel() { return reasonLabel; }
    public void setReasonLabel(String reasonLabel) { this.reasonLabel = reasonLabel; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getClaimAmountFen() { return claimAmountFen; }
    public void setClaimAmountFen(Integer claimAmountFen) { this.claimAmountFen = claimAmountFen; }
    public Integer getAdminDecisionAmountFen() { return adminDecisionAmountFen; }
    public void setAdminDecisionAmountFen(Integer adminDecisionAmountFen) { this.adminDecisionAmountFen = adminDecisionAmountFen; }
    public Integer getDepositAmountFenSnapshot() { return depositAmountFenSnapshot; }
    public void setDepositAmountFenSnapshot(Integer depositAmountFenSnapshot) { this.depositAmountFenSnapshot = depositAmountFenSnapshot; }
    public String getOrderStatusSnapshot() { return orderStatusSnapshot; }
    public void setOrderStatusSnapshot(String orderStatusSnapshot) { this.orderStatusSnapshot = orderStatusSnapshot; }
    public Long getDemanderUserId() { return demanderUserId; }
    public void setDemanderUserId(Long demanderUserId) { this.demanderUserId = demanderUserId; }
    public Long getSupplierUserId() { return supplierUserId; }
    public void setSupplierUserId(Long supplierUserId) { this.supplierUserId = supplierUserId; }
    public String getEvidenceUrls() { return evidenceUrls; }
    public void setEvidenceUrls(String evidenceUrls) { this.evidenceUrls = evidenceUrls; }
    public String getDisputeStatus() { return disputeStatus; }
    public void setDisputeStatus(String disputeStatus) { this.disputeStatus = disputeStatus; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getResolutionType() { return resolutionType; }
    public void setResolutionType(String resolutionType) { this.resolutionType = resolutionType; }
    public String getRefundStatus() { return refundStatus; }
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }
    public String getFundEffectStatus() { return fundEffectStatus; }
    public void setFundEffectStatus(String fundEffectStatus) { this.fundEffectStatus = fundEffectStatus; }
    public String getAdminActionType() { return adminActionType; }
    public void setAdminActionType(String adminActionType) { this.adminActionType = adminActionType; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
