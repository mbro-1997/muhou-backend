package com.muhou.backend.web.response;

public class PropInstanceStatusLogResponse {

    private Long id;
    private Long propInstanceId;
    private Long propId;
    private String instanceNo;
    private String qrCodeId;
    private String fromStatus;
    private String fromStatusText;
    private String toStatus;
    private String toStatusText;
    private String reason;
    private Long operatorUserId;
    private String operatorRole;
    private Long relatedOrderId;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropInstanceId() { return propInstanceId; }
    public void setPropInstanceId(Long propInstanceId) { this.propInstanceId = propInstanceId; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getInstanceNo() { return instanceNo; }
    public void setInstanceNo(String instanceNo) { this.instanceNo = instanceNo; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public String getFromStatusText() { return fromStatusText; }
    public void setFromStatusText(String fromStatusText) { this.fromStatusText = fromStatusText; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
    public String getToStatusText() { return toStatusText; }
    public void setToStatusText(String toStatusText) { this.toStatusText = toStatusText; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getOperatorUserId() { return operatorUserId; }
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
    public String getOperatorRole() { return operatorRole; }
    public void setOperatorRole(String operatorRole) { this.operatorRole = operatorRole; }
    public Long getRelatedOrderId() { return relatedOrderId; }
    public void setRelatedOrderId(Long relatedOrderId) { this.relatedOrderId = relatedOrderId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
