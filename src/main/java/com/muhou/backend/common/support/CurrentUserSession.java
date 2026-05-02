package com.muhou.backend.common.support;

public class CurrentUserSession {

    private final Long userId;
    private final String role;
    private final String token;

    public CurrentUserSession(Long userId, String role, String token) {
        this.userId = userId;
        this.role = role;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
