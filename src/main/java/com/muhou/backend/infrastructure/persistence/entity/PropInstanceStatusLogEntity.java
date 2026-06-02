package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class PropInstanceStatusLogEntity {

    private Long id;
    private Long propInstanceId;
    private Long propId;
    private String instanceNo;
    private String qrCodeId;
    private String fromStatus;
    private String toStatus;
    private String reason;
    private Long operatorUserId;
    private String operatorRole;
    private Long relatedOrderId;
    private LocalDateTime createdAt;

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
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getOperatorUserId() { return operatorUserId; }
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
    public String getOperatorRole() { return operatorRole; }
    public void setOperatorRole(String operatorRole) { this.operatorRole = operatorRole; }
    public Long getRelatedOrderId() { return relatedOrderId; }
    public void setRelatedOrderId(Long relatedOrderId) { this.relatedOrderId = relatedOrderId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
