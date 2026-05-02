package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class UserEntity {

    private Long id;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private Integer realnameVerified;
    private Integer studentVerified;
    private Integer creditScore;
    private String userStatus;
    private String registerStatus;
    private String primaryRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getRealnameVerified() { return realnameVerified; }
    public void setRealnameVerified(Integer realnameVerified) { this.realnameVerified = realnameVerified; }
    public Integer getStudentVerified() { return studentVerified; }
    public void setStudentVerified(Integer studentVerified) { this.studentVerified = studentVerified; }
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    public String getUserStatus() { return userStatus; }
    public void setUserStatus(String userStatus) { this.userStatus = userStatus; }
    public String getRegisterStatus() { return registerStatus; }
    public void setRegisterStatus(String registerStatus) { this.registerStatus = registerStatus; }
    public String getPrimaryRole() { return primaryRole; }
    public void setPrimaryRole(String primaryRole) { this.primaryRole = primaryRole; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
