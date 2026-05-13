package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class OrderFundFlowEntity {

    private Long id;
    private String flowNo;
    private Long orderId;
    private Long disputeId;
    private Long paymentId;
    private String flowType;
    private String flowDirection;
    private String payerRole;
    private Long payerUserId;
    private String receiverRole;
    private Long receiverUserId;
    private Integer amountFen;
    private String currency;
    private String channelAction;
    private String channelStatus;
    private String wechatOutNo;
    private String wechatTransactionId;
    private String wechatResponse;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFlowNo() { return flowNo; }
    public void setFlowNo(String flowNo) { this.flowNo = flowNo; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getDisputeId() { return disputeId; }
    public void setDisputeId(Long disputeId) { this.disputeId = disputeId; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public String getFlowType() { return flowType; }
    public void setFlowType(String flowType) { this.flowType = flowType; }
    public String getFlowDirection() { return flowDirection; }
    public void setFlowDirection(String flowDirection) { this.flowDirection = flowDirection; }
    public String getPayerRole() { return payerRole; }
    public void setPayerRole(String payerRole) { this.payerRole = payerRole; }
    public Long getPayerUserId() { return payerUserId; }
    public void setPayerUserId(Long payerUserId) { this.payerUserId = payerUserId; }
    public String getReceiverRole() { return receiverRole; }
    public void setReceiverRole(String receiverRole) { this.receiverRole = receiverRole; }
    public Long getReceiverUserId() { return receiverUserId; }
    public void setReceiverUserId(Long receiverUserId) { this.receiverUserId = receiverUserId; }
    public Integer getAmountFen() { return amountFen; }
    public void setAmountFen(Integer amountFen) { this.amountFen = amountFen; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getChannelAction() { return channelAction; }
    public void setChannelAction(String channelAction) { this.channelAction = channelAction; }
    public String getChannelStatus() { return channelStatus; }
    public void setChannelStatus(String channelStatus) { this.channelStatus = channelStatus; }
    public String getWechatOutNo() { return wechatOutNo; }
    public void setWechatOutNo(String wechatOutNo) { this.wechatOutNo = wechatOutNo; }
    public String getWechatTransactionId() { return wechatTransactionId; }
    public void setWechatTransactionId(String wechatTransactionId) { this.wechatTransactionId = wechatTransactionId; }
    public String getWechatResponse() { return wechatResponse; }
    public void setWechatResponse(String wechatResponse) { this.wechatResponse = wechatResponse; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
