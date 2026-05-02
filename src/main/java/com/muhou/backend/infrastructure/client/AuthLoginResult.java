package com.muhou.backend.infrastructure.client;

public class AuthLoginResult {

    private final String openId;
    private final String unionId;
    private final String sessionKey;
    private final String token;
    private final boolean mock;

    public AuthLoginResult(String openId, String unionId, String sessionKey, String token, boolean mock) {
        this.openId = openId;
        this.unionId = unionId;
        this.sessionKey = sessionKey;
        this.token = token;
        this.mock = mock;
    }

    public String getOpenId() {
        return openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getToken() {
        return token;
    }

    public boolean isMock() {
        return mock;
    }
}
