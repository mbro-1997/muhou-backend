package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class OrderAdminActionEntity {
    private Long id;
    private Long orderId;
    private Long disputeId;
    private String actionType;
    private String actionStatus;
    private String beforeOrderStatus;
    private String afterOrderStatus;
    private String beforePayStatus;
    private String afterPayStatus;
    private Integer amountFen;
    private String targetRole;
    private Long targetUserId;
    private Integer scoreDelta;
    private String reason;
    private String internalNote;
    private Long operatorUserId;
    private String orderNo;
    private String operatorName;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getDisputeId() { return disputeId; }
    public void setDisputeId(Long disputeId) { this.disputeId = disputeId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getActionStatus() { return actionStatus; }
    public void setActionStatus(String actionStatus) { this.actionStatus = actionStatus; }
    public String getBeforeOrderStatus() { return beforeOrderStatus; }
    public void setBeforeOrderStatus(String beforeOrderStatus) { this.beforeOrderStatus = beforeOrderStatus; }
    public String getAfterOrderStatus() { return afterOrderStatus; }
    public void setAfterOrderStatus(String afterOrderStatus) { this.afterOrderStatus = afterOrderStatus; }
    public String getBeforePayStatus() { return beforePayStatus; }
    public void setBeforePayStatus(String beforePayStatus) { this.beforePayStatus = beforePayStatus; }
    public String getAfterPayStatus() { return afterPayStatus; }
    public void setAfterPayStatus(String afterPayStatus) { this.afterPayStatus = afterPayStatus; }
    public Integer getAmountFen() { return amountFen; }
    public void setAmountFen(Integer amountFen) { this.amountFen = amountFen; }
    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public Long getTargetUserId() { return targetUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public Integer getScoreDelta() { return scoreDelta; }
    public void setScoreDelta(Integer scoreDelta) { this.scoreDelta = scoreDelta; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getInternalNote() { return internalNote; }
    public void setInternalNote(String internalNote) { this.internalNote = internalNote; }
    public Long getOperatorUserId() { return operatorUserId; }
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
