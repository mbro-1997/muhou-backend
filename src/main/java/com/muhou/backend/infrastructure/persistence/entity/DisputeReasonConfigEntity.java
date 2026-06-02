package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class DisputeReasonConfigEntity {
    private Long id;
    private String stage;
    private String applicantRole;
    private String reasonCode;
    private String reasonLabel;
    private String description;
    private Integer requireAmount;
    private Integer requireImages;
    private Integer enabled;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public String getApplicantRole() { return applicantRole; }
    public void setApplicantRole(String applicantRole) { this.applicantRole = applicantRole; }
    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
    public String getReasonLabel() { return reasonLabel; }
    public void setReasonLabel(String reasonLabel) { this.reasonLabel = reasonLabel; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getRequireAmount() { return requireAmount; }
    public void setRequireAmount(Integer requireAmount) { this.requireAmount = requireAmount; }
    public Integer getRequireImages() { return requireImages; }
    public void setRequireImages(Integer requireImages) { this.requireImages = requireImages; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
