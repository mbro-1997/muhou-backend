package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class FactoryApplicationSubmitRequest {

    @NotBlank(message = "factoryName is required")
    private String factoryName;
    @NotBlank(message = "unifiedSocialCreditCode is required")
    private String unifiedSocialCreditCode;
    @NotBlank(message = "businessLicenseUrl is required")
    private String businessLicenseUrl;
    @NotBlank(message = "contactName is required")
    private String contactName;
    @NotBlank(message = "contactPhone is required")
    private String contactPhone;
    @NotBlank(message = "factoryAddress is required")
    private String factoryAddress;
    private String mainBusiness;
    private String remark;

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
}
