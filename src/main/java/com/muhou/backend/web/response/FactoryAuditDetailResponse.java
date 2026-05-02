package com.muhou.backend.web.response;

public class FactoryAuditDetailResponse extends FactoryAuditResponse {

    private Long applicantUserId;
    private Long inviteCodeId;
    private String unifiedSocialCreditCode;
    private String businessLicenseUrl;
    private String factoryAddress;
    private String mainBusiness;
    private String rejectReason;

    public Long getApplicantUserId() { return applicantUserId; }
    public void setApplicantUserId(Long applicantUserId) { this.applicantUserId = applicantUserId; }
    public Long getInviteCodeId() { return inviteCodeId; }
    public void setInviteCodeId(Long inviteCodeId) { this.inviteCodeId = inviteCodeId; }
    public String getUnifiedSocialCreditCode() { return unifiedSocialCreditCode; }
    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) { this.unifiedSocialCreditCode = unifiedSocialCreditCode; }
    public String getBusinessLicenseUrl() { return businessLicenseUrl; }
    public void setBusinessLicenseUrl(String businessLicenseUrl) { this.businessLicenseUrl = businessLicenseUrl; }
    public String getFactoryAddress() { return factoryAddress; }
    public void setFactoryAddress(String factoryAddress) { this.factoryAddress = factoryAddress; }
    public String getMainBusiness() { return mainBusiness; }
    public void setMainBusiness(String mainBusiness) { this.mainBusiness = mainBusiness; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
}
