package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class AdminDisputeDecisionRequest {

    private Boolean approved;

    @NotBlank(message = "裁定理由不能为空")
    private String reason;

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
