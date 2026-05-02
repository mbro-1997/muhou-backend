package com.muhou.backend.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PropEntity {

    private Long id;
    private Long supplierUserId;
    private String propName;
    private String imageUrl;
    private String styleCode;
    private String typeCode;
    private String sizeDesc;
    private BigDecimal lengthCm;
    private BigDecimal widthCm;
    private BigDecimal heightCm;
    private String materialDesc;
    private Integer dailyRentPriceFen;
    private Integer depositAmountFen;
    private String fireResistantOption;
    private String weightDesc;
    private String transportSuggestion;
    private String propStatus;
    private String auditStatus;
    private String qrCodeId;
    private String qrCodeUrl;
    private String fillStatus;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSupplierUserId() { return supplierUserId; }
    public void setSupplierUserId(Long supplierUserId) { this.supplierUserId = supplierUserId; }
    public String getPropName() { return propName; }
    public void setPropName(String propName) { this.propName = propName; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getStyleCode() { return styleCode; }
    public void setStyleCode(String styleCode) { this.styleCode = styleCode; }
    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public String getSizeDesc() { return sizeDesc; }
    public void setSizeDesc(String sizeDesc) { this.sizeDesc = sizeDesc; }
    public BigDecimal getLengthCm() { return lengthCm; }
    public void setLengthCm(BigDecimal lengthCm) { this.lengthCm = lengthCm; }
    public BigDecimal getWidthCm() { return widthCm; }
    public void setWidthCm(BigDecimal widthCm) { this.widthCm = widthCm; }
    public BigDecimal getHeightCm() { return heightCm; }
    public void setHeightCm(BigDecimal heightCm) { this.heightCm = heightCm; }
    public String getMaterialDesc() { return materialDesc; }
    public void setMaterialDesc(String materialDesc) { this.materialDesc = materialDesc; }
    public Integer getDailyRentPriceFen() { return dailyRentPriceFen; }
    public void setDailyRentPriceFen(Integer dailyRentPriceFen) { this.dailyRentPriceFen = dailyRentPriceFen; }
    public Integer getDepositAmountFen() { return depositAmountFen; }
    public void setDepositAmountFen(Integer depositAmountFen) { this.depositAmountFen = depositAmountFen; }
    public String getFireResistantOption() { return fireResistantOption; }
    public void setFireResistantOption(String fireResistantOption) { this.fireResistantOption = fireResistantOption; }
    public String getWeightDesc() { return weightDesc; }
    public void setWeightDesc(String weightDesc) { this.weightDesc = weightDesc; }
    public String getTransportSuggestion() { return transportSuggestion; }
    public void setTransportSuggestion(String transportSuggestion) { this.transportSuggestion = transportSuggestion; }
    public String getPropStatus() { return propStatus; }
    public void setPropStatus(String propStatus) { this.propStatus = propStatus; }
    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public String getQrCodeUrl() { return qrCodeUrl; }
    public void setQrCodeUrl(String qrCodeUrl) { this.qrCodeUrl = qrCodeUrl; }
    public String getFillStatus() { return fillStatus; }
    public void setFillStatus(String fillStatus) { this.fillStatus = fillStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
