package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class RentalOrderItemEntity {

    private Long id;
    private Long orderId;
    private Long propId;
    private String propNameSnapshot;
    private String imageUrlSnapshot;
    private Integer dailyRentPriceFenSnapshot;
    private Integer depositAmountFenSnapshot;
    private String outboundStatus;
    private String returnStatus;
    private LocalDateTime outboundScannedAt;
    private LocalDateTime returnScannedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getPropNameSnapshot() { return propNameSnapshot; }
    public void setPropNameSnapshot(String propNameSnapshot) { this.propNameSnapshot = propNameSnapshot; }
    public String getImageUrlSnapshot() { return imageUrlSnapshot; }
    public void setImageUrlSnapshot(String imageUrlSnapshot) { this.imageUrlSnapshot = imageUrlSnapshot; }
    public Integer getDailyRentPriceFenSnapshot() { return dailyRentPriceFenSnapshot; }
    public void setDailyRentPriceFenSnapshot(Integer dailyRentPriceFenSnapshot) { this.dailyRentPriceFenSnapshot = dailyRentPriceFenSnapshot; }
    public Integer getDepositAmountFenSnapshot() { return depositAmountFenSnapshot; }
    public void setDepositAmountFenSnapshot(Integer depositAmountFenSnapshot) { this.depositAmountFenSnapshot = depositAmountFenSnapshot; }
    public String getOutboundStatus() { return outboundStatus; }
    public void setOutboundStatus(String outboundStatus) { this.outboundStatus = outboundStatus; }
    public String getReturnStatus() { return returnStatus; }
    public void setReturnStatus(String returnStatus) { this.returnStatus = returnStatus; }
    public LocalDateTime getOutboundScannedAt() { return outboundScannedAt; }
    public void setOutboundScannedAt(LocalDateTime outboundScannedAt) { this.outboundScannedAt = outboundScannedAt; }
    public LocalDateTime getReturnScannedAt() { return returnScannedAt; }
    public void setReturnScannedAt(LocalDateTime returnScannedAt) { this.returnScannedAt = returnScannedAt; }
}
