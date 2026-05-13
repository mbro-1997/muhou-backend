package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class UserCreditLogEntity {

    private Long id;
    private Long userId;
    private String changeType;
    private String bizType;
    private Long bizId;
    private Integer beforeScore;
    private Integer deltaScore;
    private Integer afterScore;
    private String reason;
    private Long operatorUserId;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public Integer getBeforeScore() { return beforeScore; }
    public void setBeforeScore(Integer beforeScore) { this.beforeScore = beforeScore; }
    public Integer getDeltaScore() { return deltaScore; }
    public void setDeltaScore(Integer deltaScore) { this.deltaScore = deltaScore; }
    public Integer getAfterScore() { return afterScore; }
    public void setAfterScore(Integer afterScore) { this.afterScore = afterScore; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getOperatorUserId() { return operatorUserId; }
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
