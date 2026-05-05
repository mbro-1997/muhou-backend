package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class PropQrCodeEntity {

    private Long id;
    private Long propId;
    private String qrCodeId;
    private String qrScene;
    private String qrPage;
    private String qrImageUrl;
    private String qrImageStorageKey;
    private String imageSha256;
    private String status;
    private Integer printedFlag;
    private LocalDateTime printedAt;
    private LocalDateTime downloadedAt;
    private Long createdByAdminUserId;
    private Long usedBySupplierUserId;
    private LocalDateTime usedAt;
    private Long revokedByAdminUserId;
    private LocalDateTime revokedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public String getQrScene() { return qrScene; }
    public void setQrScene(String qrScene) { this.qrScene = qrScene; }
    public String getQrPage() { return qrPage; }
    public void setQrPage(String qrPage) { this.qrPage = qrPage; }
    public String getQrImageUrl() { return qrImageUrl; }
    public void setQrImageUrl(String qrImageUrl) { this.qrImageUrl = qrImageUrl; }
    public String getQrImageStorageKey() { return qrImageStorageKey; }
    public void setQrImageStorageKey(String qrImageStorageKey) { this.qrImageStorageKey = qrImageStorageKey; }
    public String getImageSha256() { return imageSha256; }
    public void setImageSha256(String imageSha256) { this.imageSha256 = imageSha256; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPrintedFlag() { return printedFlag; }
    public void setPrintedFlag(Integer printedFlag) { this.printedFlag = printedFlag; }
    public LocalDateTime getPrintedAt() { return printedAt; }
    public void setPrintedAt(LocalDateTime printedAt) { this.printedAt = printedAt; }
    public LocalDateTime getDownloadedAt() { return downloadedAt; }
    public void setDownloadedAt(LocalDateTime downloadedAt) { this.downloadedAt = downloadedAt; }
    public Long getCreatedByAdminUserId() { return createdByAdminUserId; }
    public void setCreatedByAdminUserId(Long createdByAdminUserId) { this.createdByAdminUserId = createdByAdminUserId; }
    public Long getUsedBySupplierUserId() { return usedBySupplierUserId; }
    public void setUsedBySupplierUserId(Long usedBySupplierUserId) { this.usedBySupplierUserId = usedBySupplierUserId; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
    public Long getRevokedByAdminUserId() { return revokedByAdminUserId; }
    public void setRevokedByAdminUserId(Long revokedByAdminUserId) { this.revokedByAdminUserId = revokedByAdminUserId; }
    public LocalDateTime getRevokedAt() { return revokedAt; }
    public void setRevokedAt(LocalDateTime revokedAt) { this.revokedAt = revokedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
