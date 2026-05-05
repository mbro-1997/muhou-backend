package com.muhou.backend.web.response;

import java.util.List;

public class UserProfileResponse {

    private Long id;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String currentRole;
    private List<String> roleBindings;
    private String registerStatus;
    private String defaultEntry;
    private boolean needRoleSelection;
    private String factoryAuditStatus;
    private String factoryAuditRejectReason;
    private boolean realnameVerified;
    private boolean studentVerified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCurrentRole() { return currentRole; }
    public void setCurrentRole(String currentRole) { this.currentRole = currentRole; }
    public List<String> getRoleBindings() { return roleBindings; }
    public void setRoleBindings(List<String> roleBindings) { this.roleBindings = roleBindings; }
    public String getRegisterStatus() { return registerStatus; }
    public void setRegisterStatus(String registerStatus) { this.registerStatus = registerStatus; }
    public String getDefaultEntry() { return defaultEntry; }
    public void setDefaultEntry(String defaultEntry) { this.defaultEntry = defaultEntry; }
    public boolean isNeedRoleSelection() { return needRoleSelection; }
    public void setNeedRoleSelection(boolean needRoleSelection) { this.needRoleSelection = needRoleSelection; }
    public String getFactoryAuditStatus() { return factoryAuditStatus; }
    public void setFactoryAuditStatus(String factoryAuditStatus) { this.factoryAuditStatus = factoryAuditStatus; }
    public String getFactoryAuditRejectReason() { return factoryAuditRejectReason; }
    public void setFactoryAuditRejectReason(String factoryAuditRejectReason) { this.factoryAuditRejectReason = factoryAuditRejectReason; }
    public boolean isRealnameVerified() { return realnameVerified; }
    public void setRealnameVerified(boolean realnameVerified) { this.realnameVerified = realnameVerified; }
    public boolean isStudentVerified() { return studentVerified; }
    public void setStudentVerified(boolean studentVerified) { this.studentVerified = studentVerified; }
}
