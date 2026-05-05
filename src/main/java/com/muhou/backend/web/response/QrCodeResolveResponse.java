package com.muhou.backend.web.response;

public class QrCodeResolveResponse {

    private String qrCodeId;
    private String qrStatus;
    private Long propId;
    private String propName;
    private String fillStatus;
    private String auditStatus;
    private String propStatus;
    private String redirectType;
    private String message;

    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public String getQrStatus() { return qrStatus; }
    public void setQrStatus(String qrStatus) { this.qrStatus = qrStatus; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getPropName() { return propName; }
    public void setPropName(String propName) { this.propName = propName; }
    public String getFillStatus() { return fillStatus; }
    public void setFillStatus(String fillStatus) { this.fillStatus = fillStatus; }
    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }
    public String getPropStatus() { return propStatus; }
    public void setPropStatus(String propStatus) { this.propStatus = propStatus; }
    public String getRedirectType() { return redirectType; }
    public void setRedirectType(String redirectType) { this.redirectType = redirectType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
