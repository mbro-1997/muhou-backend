package com.muhou.backend.web.request;

import jakarta.validation.constraints.Size;

public class UserProfileUpdateRequest {

    @Size(max = 64, message = "nickname must not exceed 64 chars")
    private String nickname;

    @Size(max = 500, message = "avatarUrl must not exceed 500 chars")
    private String avatarUrl;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
