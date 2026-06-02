package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class AdminDisputeDecisionRequest {

    @NotBlank(message = "裁定动作不能为空")
    private String adminActionType;

    private BigDecimal decisionAmount;

    @NotBlank(message = "裁定理由不能为空")
    private String reason;

    public String getAdminActionType() { return adminActionType; }
    public void setAdminActionType(String adminActionType) { this.adminActionType = adminActionType; }
    public BigDecimal getDecisionAmount() { return decisionAmount; }
    public void setDecisionAmount(BigDecimal decisionAmount) { this.decisionAmount = decisionAmount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
