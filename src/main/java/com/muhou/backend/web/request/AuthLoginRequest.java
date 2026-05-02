package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;

public class AuthLoginRequest {

    @NotBlank(message = "code is required")
    private String code;
    private String desiredRole;
    private Boolean realWechat;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesiredRole() {
        return desiredRole;
    }

    public void setDesiredRole(String desiredRole) {
        this.desiredRole = desiredRole;
    }

    public Boolean getRealWechat() {
        return realWechat;
    }

    public void setRealWechat(Boolean realWechat) {
        this.realWechat = realWechat;
    }
}
