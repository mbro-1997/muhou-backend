package com.muhou.backend.web.response;

public class AuthLoginResponse {

    private String token;
    private Long userId;
    private String openId;
    private String sessionKey;
    private String currentRole;
    private java.util.List<String> roleBindings;
    private String registerStatus;
    private String defaultEntry;
    private boolean needRoleSelection;
    private String factoryAuditStatus;
    private String factoryAuditRejectReason;
    private boolean mock;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getOpenId() { return openId; }
    public void setOpenId(String openId) { this.openId = openId; }
    public String getSessionKey() { return sessionKey; }
    public void setSessionKey(String sessionKey) { this.sessionKey = sessionKey; }
    public String getCurrentRole() { return currentRole; }
    public void setCurrentRole(String currentRole) { this.currentRole = currentRole; }
    public java.util.List<String> getRoleBindings() { return roleBindings; }
    public void setRoleBindings(java.util.List<String> roleBindings) { this.roleBindings = roleBindings; }
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
    public boolean isMock() { return mock; }
    public void setMock(boolean mock) { this.mock = mock; }
}
