package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class OrderPaymentEntity {

    private Long id;
    private String paymentNo;
    private Long orderId;
    private String payScene;
    private String paymentChannel;
    private String paymentStatus;
    private String merchantMchid;
    private String appid;
    private String openid;
    private String merchantOutTradeNo;
    private String wxTransactionId;
    private String wxPrepayId;
    private String description;
    private Integer amountFen;
    private String currency;
    private String clientIp;
    private String notifyUrl;
    private String attachData;
    private LocalDateTime timeExpire;
    private LocalDateTime successTime;
    private String failReason;
    private String closedReason;
    private String rawCreateResponse;
    private String rawQueryResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPaymentNo() { return paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getPayScene() { return payScene; }
    public void setPayScene(String payScene) { this.payScene = payScene; }
    public String getPaymentChannel() { return paymentChannel; }
    public void setPaymentChannel(String paymentChannel) { this.paymentChannel = paymentChannel; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getMerchantMchid() { return merchantMchid; }
    public void setMerchantMchid(String merchantMchid) { this.merchantMchid = merchantMchid; }
    public String getAppid() { return appid; }
    public void setAppid(String appid) { this.appid = appid; }
    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }
    public String getMerchantOutTradeNo() { return merchantOutTradeNo; }
    public void setMerchantOutTradeNo(String merchantOutTradeNo) { this.merchantOutTradeNo = merchantOutTradeNo; }
    public String getWxTransactionId() { return wxTransactionId; }
    public void setWxTransactionId(String wxTransactionId) { this.wxTransactionId = wxTransactionId; }
    public String getWxPrepayId() { return wxPrepayId; }
    public void setWxPrepayId(String wxPrepayId) { this.wxPrepayId = wxPrepayId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getAmountFen() { return amountFen; }
    public void setAmountFen(Integer amountFen) { this.amountFen = amountFen; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String notifyUrl) { this.notifyUrl = notifyUrl; }
    public String getAttachData() { return attachData; }
    public void setAttachData(String attachData) { this.attachData = attachData; }
    public LocalDateTime getTimeExpire() { return timeExpire; }
    public void setTimeExpire(LocalDateTime timeExpire) { this.timeExpire = timeExpire; }
    public LocalDateTime getSuccessTime() { return successTime; }
    public void setSuccessTime(LocalDateTime successTime) { this.successTime = successTime; }
    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }
    public String getClosedReason() { return closedReason; }
    public void setClosedReason(String closedReason) { this.closedReason = closedReason; }
    public String getRawCreateResponse() { return rawCreateResponse; }
    public void setRawCreateResponse(String rawCreateResponse) { this.rawCreateResponse = rawCreateResponse; }
    public String getRawQueryResponse() { return rawQueryResponse; }
    public void setRawQueryResponse(String rawQueryResponse) { this.rawQueryResponse = rawQueryResponse; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
