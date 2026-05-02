package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class PropAuditEntity {

    private Long id;
    private Long propId;
    private String actionType;
    private String auditStatus;
    private String applyRemark;
    private String auditRemark;
    private Long reviewerId;
    private String propName;
    private String imageUrl;
    private String styleCode;
    private String typeCode;
    private String sizeDesc;
    private java.math.BigDecimal lengthCm;
    private java.math.BigDecimal widthCm;
    private java.math.BigDecimal heightCm;
    private String materialDesc;
    private Integer dailyRentPriceFen;
    private Integer depositAmountFen;
    private String fireResistantOption;
    private String weightDesc;
    private String transportSuggestion;
    private String propStatus;
    private String propAuditStatus;
    private String fillStatus;
    private String qrCodeId;
    private Long supplierUserId;
    private String supplierNickname;
    private String supplierPhone;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }
    public String getApplyRemark() { return applyRemark; }
    public void setApplyRemark(String applyRemark) { this.applyRemark = applyRemark; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
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
    public java.math.BigDecimal getLengthCm() { return lengthCm; }
    public void setLengthCm(java.math.BigDecimal lengthCm) { this.lengthCm = lengthCm; }
    public java.math.BigDecimal getWidthCm() { return widthCm; }
    public void setWidthCm(java.math.BigDecimal widthCm) { this.widthCm = widthCm; }
    public java.math.BigDecimal getHeightCm() { return heightCm; }
    public void setHeightCm(java.math.BigDecimal heightCm) { this.heightCm = heightCm; }
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
    public String getPropAuditStatus() { return propAuditStatus; }
    public void setPropAuditStatus(String propAuditStatus) { this.propAuditStatus = propAuditStatus; }
    public String getFillStatus() { return fillStatus; }
    public void setFillStatus(String fillStatus) { this.fillStatus = fillStatus; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public Long getSupplierUserId() { return supplierUserId; }
    public void setSupplierUserId(Long supplierUserId) { this.supplierUserId = supplierUserId; }
    public String getSupplierNickname() { return supplierNickname; }
    public void setSupplierNickname(String supplierNickname) { this.supplierNickname = supplierNickname; }
    public String getSupplierPhone() { return supplierPhone; }
    public void setSupplierPhone(String supplierPhone) { this.supplierPhone = supplierPhone; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}
