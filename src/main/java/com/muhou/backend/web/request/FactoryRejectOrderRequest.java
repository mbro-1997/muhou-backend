package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class FactoryRejectOrderRequest {

    @NotBlank(message = "rejectReason is required")
    private String rejectReason;

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
