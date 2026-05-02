package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class AdminBindRoleRequest {

    @NotBlank(message = "role is required")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
