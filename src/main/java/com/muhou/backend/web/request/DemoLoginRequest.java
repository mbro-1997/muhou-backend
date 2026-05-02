package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class DemoLoginRequest {

    @NotBlank(message = "accountKey is required")
    private String accountKey;

    private String role;

    private Boolean resetDemo;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getResetDemo() {
        return resetDemo;
    }

    public void setResetDemo(Boolean resetDemo) {
        this.resetDemo = resetDemo;
    }
}
