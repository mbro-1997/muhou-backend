package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class PropStatusUpdateRequest {

    @NotBlank(message = "targetStatus is required")
    private String targetStatus;

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }
}
