package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class PaymentOrderLinkEntity {

    private Long id;
    private Long paymentId;
    private Long orderId;
    private String orderNo;
    private Long supplierUserId;
    private Integer rentAmountFen;
    private Integer depositAmountFen;
    private Integer totalAmountFen;
    private Integer refundAmountFen;
    private Integer settlementAmountFen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getSupplierUserId() { return supplierUserId; }
    public void setSupplierUserId(Long supplierUserId) { this.supplierUserId = supplierUserId; }
    public Integer getRentAmountFen() { return rentAmountFen; }
    public void setRentAmountFen(Integer rentAmountFen) { this.rentAmountFen = rentAmountFen; }
    public Integer getDepositAmountFen() { return depositAmountFen; }
    public void setDepositAmountFen(Integer depositAmountFen) { this.depositAmountFen = depositAmountFen; }
    public Integer getTotalAmountFen() { return totalAmountFen; }
    public void setTotalAmountFen(Integer totalAmountFen) { this.totalAmountFen = totalAmountFen; }
    public Integer getRefundAmountFen() { return refundAmountFen; }
    public void setRefundAmountFen(Integer refundAmountFen) { this.refundAmountFen = refundAmountFen; }
    public Integer getSettlementAmountFen() { return settlementAmountFen; }
    public void setSettlementAmountFen(Integer settlementAmountFen) { this.settlementAmountFen = settlementAmountFen; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
