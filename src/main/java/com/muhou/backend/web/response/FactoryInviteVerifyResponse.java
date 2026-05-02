package com.muhou.backend.web.response;

public class FactoryInviteVerifyResponse {

    private Long inviteCodeId;
    private String codeSuffix;
    private String status;
    private String expireAt;

    public Long getInviteCodeId() { return inviteCodeId; }
    public void setInviteCodeId(Long inviteCodeId) { this.inviteCodeId = inviteCodeId; }
    public String getCodeSuffix() { return codeSuffix; }
    public void setCodeSuffix(String codeSuffix) { this.codeSuffix = codeSuffix; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getExpireAt() { return expireAt; }
    public void setExpireAt(String expireAt) { this.expireAt = expireAt; }
}
