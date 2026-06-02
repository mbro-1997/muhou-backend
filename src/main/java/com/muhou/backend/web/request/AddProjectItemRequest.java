package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotNull;

public class AddProjectItemRequest {

    @NotNull(message = "propId is required")
    private Long propId;
    private Integer quantity;

    public Long getPropId() {
        return propId;
    }

    public void setPropId(Long propId) {
        this.propId = propId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
