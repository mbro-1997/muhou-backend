package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class PropInstanceEntity {

    private Long id;
    private Long propId;
    private String qrCodeId;
    private String instanceNo;
    private String instanceStatus;
    private Long currentOrderId;
    private String remark;
    private String statusRemark;
    private LocalDateTime statusChangedAt;
    private Long statusChangedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public String getInstanceNo() { return instanceNo; }
    public void setInstanceNo(String instanceNo) { this.instanceNo = instanceNo; }
    public String getInstanceStatus() { return instanceStatus; }
    public void setInstanceStatus(String instanceStatus) { this.instanceStatus = instanceStatus; }
    public Long getCurrentOrderId() { return currentOrderId; }
    public void setCurrentOrderId(Long currentOrderId) { this.currentOrderId = currentOrderId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatusRemark() { return statusRemark; }
    public void setStatusRemark(String statusRemark) { this.statusRemark = statusRemark; }
    public LocalDateTime getStatusChangedAt() { return statusChangedAt; }
    public void setStatusChangedAt(LocalDateTime statusChangedAt) { this.statusChangedAt = statusChangedAt; }
    public Long getStatusChangedBy() { return statusChangedBy; }
    public void setStatusChangedBy(Long statusChangedBy) { this.statusChangedBy = statusChangedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
