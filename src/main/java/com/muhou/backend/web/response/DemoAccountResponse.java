package com.muhou.backend.web.response;

import java.util.List;

public class DemoAccountResponse {

    private String accountKey;
    private Long userId;
    private String nickname;
    private String description;
    private List<String> roleBindings;
    private String registerStatus;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRoleBindings() {
        return roleBindings;
    }

    public void setRoleBindings(List<String> roleBindings) {
        this.roleBindings = roleBindings;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }
}
