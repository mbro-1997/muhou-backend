package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class AdminReviewEntity {

    private Long id;
    private Long orderId;
    private String orderNo;
    private Long reviewerUserId;
    private String reviewerName;
    private String reviewerRole;
    private Integer score;
    private Integer propScore;
    private Integer counterpartyScore;
    private String content;
    private Integer visibleFlag;
    private Integer autoFlag;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getReviewerUserId() { return reviewerUserId; }
    public void setReviewerUserId(Long reviewerUserId) { this.reviewerUserId = reviewerUserId; }
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    public String getReviewerRole() { return reviewerRole; }
    public void setReviewerRole(String reviewerRole) { this.reviewerRole = reviewerRole; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getPropScore() { return propScore; }
    public void setPropScore(Integer propScore) { this.propScore = propScore; }
    public Integer getCounterpartyScore() { return counterpartyScore; }
    public void setCounterpartyScore(Integer counterpartyScore) { this.counterpartyScore = counterpartyScore; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getVisibleFlag() { return visibleFlag; }
    public void setVisibleFlag(Integer visibleFlag) { this.visibleFlag = visibleFlag; }
    public Integer getAutoFlag() { return autoFlag; }
    public void setAutoFlag(Integer autoFlag) { this.autoFlag = autoFlag; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
