package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class PropInstanceStatusUpdateRequest {

    @NotBlank
    private String targetStatus;

    @NotBlank
    private String reason;

    private Long relatedOrderId;

    public String getTargetStatus() { return targetStatus; }
    public void setTargetStatus(String targetStatus) { this.targetStatus = targetStatus; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getRelatedOrderId() { return relatedOrderId; }
    public void setRelatedOrderId(Long relatedOrderId) { this.relatedOrderId = relatedOrderId; }
}
