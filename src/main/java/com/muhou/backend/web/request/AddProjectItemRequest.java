package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotNull;

public class AddProjectItemRequest {

    @NotNull(message = "propId is required")
    private Long propId;

    public Long getPropId() {
        return propId;
    }

    public void setPropId(Long propId) {
        this.propId = propId;
    }
}
