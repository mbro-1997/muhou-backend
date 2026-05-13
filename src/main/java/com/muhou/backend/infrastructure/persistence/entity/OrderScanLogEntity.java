package com.muhou.backend.infrastructure.persistence.entity;

public class OrderScanLogEntity {

    private Long orderId;
    private Long orderItemId;
    private Long propId;
    private String qrCodeId;
    private String scanType;
    private String scanStatus;
    private Long scanUserId;
    private Long supplierUserId;
    private String rawScanResult;
    private String failReason;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }
    public String getScanStatus() { return scanStatus; }
    public void setScanStatus(String scanStatus) { this.scanStatus = scanStatus; }
    public Long getScanUserId() { return scanUserId; }
    public void setScanUserId(Long scanUserId) { this.scanUserId = scanUserId; }
    public Long getSupplierUserId() { return supplierUserId; }
    public void setSupplierUserId(Long supplierUserId) { this.supplierUserId = supplierUserId; }
    public String getRawScanResult() { return rawScanResult; }
    public void setRawScanResult(String rawScanResult) { this.rawScanResult = rawScanResult; }
    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }
}
