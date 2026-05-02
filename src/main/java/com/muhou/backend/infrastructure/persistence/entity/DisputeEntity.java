package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class DisputeEntity {

    private Long id;
    private Long orderId;
    private Long applyUserId;
    private String title;
    private String content;
    private String disputeStatus;
    private String resolution;
    private Long reviewerId;
    private String applicantName;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getApplyUserId() { return applyUserId; }
    public void setApplyUserId(Long applyUserId) { this.applyUserId = applyUserId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getDisputeStatus() { return disputeStatus; }
    public void setDisputeStatus(String disputeStatus) { this.disputeStatus = disputeStatus; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
