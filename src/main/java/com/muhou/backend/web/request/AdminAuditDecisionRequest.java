package com.muhou.backend.web.request;

public class AdminAuditDecisionRequest {

    private Boolean approved;
    private String remark;

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
