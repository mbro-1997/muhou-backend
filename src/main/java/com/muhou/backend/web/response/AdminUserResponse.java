package com.muhou.backend.web.response;

import java.util.List;

public class AdminUserResponse {

    private Long id;
    private String name;
    private String phone;
    private String role;
    private List<String> roleBindings;
    private boolean verified;
    private String verifiedText;
    private String status;
    private String statusText;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<String> getRoleBindings() { return roleBindings; }
    public void setRoleBindings(List<String> roleBindings) { this.roleBindings = roleBindings; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public String getVerifiedText() { return verifiedText; }
    public void setVerifiedText(String verifiedText) { this.verifiedText = verifiedText; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
}
