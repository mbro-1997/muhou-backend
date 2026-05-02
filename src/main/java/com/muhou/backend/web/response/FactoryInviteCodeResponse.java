package com.muhou.backend.web.response;

public class FactoryInviteCodeResponse {

    private Long id;
    private String inviteCode;
    private String codeSuffix;
    private String status;
    private String statusText;
    private String createdAt;
    private String expireAt;
    private String remark;
    private Long lockedByUserId;
    private Long usedByUserId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInviteCode() { return inviteCode; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
    public String getCodeSuffix() { return codeSuffix; }
    public void setCodeSuffix(String codeSuffix) { this.codeSuffix = codeSuffix; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getExpireAt() { return expireAt; }
    public void setExpireAt(String expireAt) { this.expireAt = expireAt; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getLockedByUserId() { return lockedByUserId; }
    public void setLockedByUserId(Long lockedByUserId) { this.lockedByUserId = lockedByUserId; }
    public Long getUsedByUserId() { return usedByUserId; }
    public void setUsedByUserId(Long usedByUserId) { this.usedByUserId = usedByUserId; }
}
