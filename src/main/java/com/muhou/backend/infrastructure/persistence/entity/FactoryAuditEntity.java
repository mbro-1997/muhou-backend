package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class FactoryAuditEntity {

    private Long id;
    private Long userId;
    private Long applicantUserId;
    private Long inviteCodeId;
    private String companyName;
    private String factoryName;
    private String unifiedSocialCreditCode;
    private String businessLicenseUrl;
    private String contactName;
    private String contactPhone;
    private String factoryAddress;
    private String mainBusiness;
    private String remark;
    private String auditStatus;
    private String auditRemark;
    private String rejectReason;
    private Long reviewerId;
    private Long reviewedByAdminUserId;
    private Long createdFactoryId;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getApplicantUserId() { return applicantUserId; }
    public void setApplicantUserId(Long applicantUserId) { this.applicantUserId = applicantUserId; }
    public Long getInviteCodeId() { return inviteCodeId; }
    public void setInviteCodeId(Long inviteCodeId) { this.inviteCodeId = inviteCodeId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getFactoryName() { return factoryName; }
    public void setFactoryName(String factoryName) { this.factoryName = factoryName; }
    public String getUnifiedSocialCreditCode() { return unifiedSocialCreditCode; }
    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) { this.unifiedSocialCreditCode = unifiedSocialCreditCode; }
    public String getBusinessLicenseUrl() { return businessLicenseUrl; }
    public void setBusinessLicenseUrl(String businessLicenseUrl) { this.businessLicenseUrl = businessLicenseUrl; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getFactoryAddress() { return factoryAddress; }
    public void setFactoryAddress(String factoryAddress) { this.factoryAddress = factoryAddress; }
    public String getMainBusiness() { return mainBusiness; }
    public void setMainBusiness(String mainBusiness) { this.mainBusiness = mainBusiness; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
    public Long getReviewedByAdminUserId() { return reviewedByAdminUserId; }
    public void setReviewedByAdminUserId(Long reviewedByAdminUserId) { this.reviewedByAdminUserId = reviewedByAdminUserId; }
    public Long getCreatedFactoryId() { return createdFactoryId; }
    public void setCreatedFactoryId(Long createdFactoryId) { this.createdFactoryId = createdFactoryId; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
