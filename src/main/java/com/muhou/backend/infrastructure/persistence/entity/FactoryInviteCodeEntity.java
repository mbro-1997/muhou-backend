package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class FactoryInviteCodeEntity {

    private Long id;
    private String codeHash;
    private String codePlain;
    private String codeSuffix;
    private String status;
    private LocalDateTime expireAt;
    private Long createdByAdminUserId;
    private Long lockedByUserId;
    private LocalDateTime lockedAt;
    private Long usedByUserId;
    private LocalDateTime usedAt;
    private Long revokedByAdminUserId;
    private LocalDateTime revokedAt;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodeHash() { return codeHash; }
    public void setCodeHash(String codeHash) { this.codeHash = codeHash; }
    public String getCodePlain() { return codePlain; }
    public void setCodePlain(String codePlain) { this.codePlain = codePlain; }
    public String getCodeSuffix() { return codeSuffix; }
    public void setCodeSuffix(String codeSuffix) { this.codeSuffix = codeSuffix; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getExpireAt() { return expireAt; }
    public void setExpireAt(LocalDateTime expireAt) { this.expireAt = expireAt; }
    public Long getCreatedByAdminUserId() { return createdByAdminUserId; }
    public void setCreatedByAdminUserId(Long createdByAdminUserId) { this.createdByAdminUserId = createdByAdminUserId; }
    public Long getLockedByUserId() { return lockedByUserId; }
    public void setLockedByUserId(Long lockedByUserId) { this.lockedByUserId = lockedByUserId; }
    public LocalDateTime getLockedAt() { return lockedAt; }
    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }
    public Long getUsedByUserId() { return usedByUserId; }
    public void setUsedByUserId(Long usedByUserId) { this.usedByUserId = usedByUserId; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
    public Long getRevokedByAdminUserId() { return revokedByAdminUserId; }
    public void setRevokedByAdminUserId(Long revokedByAdminUserId) { this.revokedByAdminUserId = revokedByAdminUserId; }
    public LocalDateTime getRevokedAt() { return revokedAt; }
    public void setRevokedAt(LocalDateTime revokedAt) { this.revokedAt = revokedAt; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
