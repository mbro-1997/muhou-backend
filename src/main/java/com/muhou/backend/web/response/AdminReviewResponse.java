package com.muhou.backend.web.response;

public class AdminReviewResponse {

    private Long id;
    private Long orderId;
    private String orderNo;
    private String user;
    private String reviewerRole;
    private Integer score;
    private Integer propScore;
    private Integer counterpartyScore;
    private String content;
    private boolean visible;
    private boolean auto;
    private String visibleText;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
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
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isAuto() { return auto; }
    public void setAuto(boolean auto) { this.auto = auto; }
    public String getVisibleText() { return visibleText; }
    public void setVisibleText(String visibleText) { this.visibleText = visibleText; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
