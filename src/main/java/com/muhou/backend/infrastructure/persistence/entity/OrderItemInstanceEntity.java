package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class OrderItemInstanceEntity {

    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long propId;
    private Long propInstanceId;
    private String outboundStatus;
    private String returnStatus;
    private LocalDateTime outboundScannedAt;
    private LocalDateTime returnScannedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public Long getPropInstanceId() { return propInstanceId; }
    public void setPropInstanceId(Long propInstanceId) { this.propInstanceId = propInstanceId; }
    public String getOutboundStatus() { return outboundStatus; }
    public void setOutboundStatus(String outboundStatus) { this.outboundStatus = outboundStatus; }
    public String getReturnStatus() { return returnStatus; }
    public void setReturnStatus(String returnStatus) { this.returnStatus = returnStatus; }
    public LocalDateTime getOutboundScannedAt() { return outboundScannedAt; }
    public void setOutboundScannedAt(LocalDateTime outboundScannedAt) { this.outboundScannedAt = outboundScannedAt; }
    public LocalDateTime getReturnScannedAt() { return returnScannedAt; }
    public void setReturnScannedAt(LocalDateTime returnScannedAt) { this.returnScannedAt = returnScannedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
