package com.muhou.backend.web.response;

public class FactoryInviteCreateResponse {

    private Long id;
    private String inviteCode;
    private String codeSuffix;
    private String expireAt;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInviteCode() { return inviteCode; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
    public String getCodeSuffix() { return codeSuffix; }
    public void setCodeSuffix(String codeSuffix) { this.codeSuffix = codeSuffix; }
    public String getExpireAt() { return expireAt; }
    public void setExpireAt(String expireAt) { this.expireAt = expireAt; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
