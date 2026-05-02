package com.muhou.backend.web.response;

public class PayResponse {

    private Long orderId;
    private Long paymentId;
    private String paymentNo;
    private String merchantOutTradeNo;
    private String prepayId;
    private String transactionId;
    private String payStatus;
    private boolean mock;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public String getPaymentNo() { return paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    public String getMerchantOutTradeNo() { return merchantOutTradeNo; }
    public void setMerchantOutTradeNo(String merchantOutTradeNo) { this.merchantOutTradeNo = merchantOutTradeNo; }
    public String getPrepayId() { return prepayId; }
    public void setPrepayId(String prepayId) { this.prepayId = prepayId; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }
    public boolean isMock() { return mock; }
    public void setMock(boolean mock) { this.mock = mock; }
}
