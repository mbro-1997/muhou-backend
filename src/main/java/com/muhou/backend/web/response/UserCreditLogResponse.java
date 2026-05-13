package com.muhou.backend.web.response;

public class UserCreditLogResponse {

    private Long id;
    private Integer beforeScore;
    private Integer deltaScore;
    private Integer afterScore;
    private String changeType;
    private String changeTypeText;
    private String bizType;
    private Long bizId;
    private String reason;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getBeforeScore() { return beforeScore; }
    public void setBeforeScore(Integer beforeScore) { this.beforeScore = beforeScore; }
    public Integer getDeltaScore() { return deltaScore; }
    public void setDeltaScore(Integer deltaScore) { this.deltaScore = deltaScore; }
    public Integer getAfterScore() { return afterScore; }
    public void setAfterScore(Integer afterScore) { this.afterScore = afterScore; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public String getChangeTypeText() { return changeTypeText; }
    public void setChangeTypeText(String changeTypeText) { this.changeTypeText = changeTypeText; }
    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
