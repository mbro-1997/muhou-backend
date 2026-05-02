package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class FactoryProfileEntity {

    private Long id;
    private Long ownerUserId;
    private String factoryName;
    private String unifiedSocialCreditCode;
    private String businessLicenseUrl;
    private String contactName;
    private String contactPhone;
    private String factoryAddress;
    private String mainBusiness;
    private String status;
    private Long approvedAuditId;
    private Long approvedByAdminUserId;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(Long ownerUserId) { this.ownerUserId = ownerUserId; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getApprovedAuditId() { return approvedAuditId; }
    public void setApprovedAuditId(Long approvedAuditId) { this.approvedAuditId = approvedAuditId; }
    public Long getApprovedByAdminUserId() { return approvedByAdminUserId; }
    public void setApprovedByAdminUserId(Long approvedByAdminUserId) { this.approvedByAdminUserId = approvedByAdminUserId; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
