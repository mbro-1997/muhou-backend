package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDateTime;

public class PropImageEntity {

    private Long id;
    private Long propId;
    private String imageUrl;
    private Integer sortOrder;
    private Integer mainFlag;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getMainFlag() { return mainFlag; }
    public void setMainFlag(Integer mainFlag) { this.mainFlag = mainFlag; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
