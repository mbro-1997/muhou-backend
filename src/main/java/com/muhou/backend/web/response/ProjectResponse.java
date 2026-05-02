package com.muhou.backend.web.response;

import java.util.List;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String statusText;
    private Integer unavailableCount;
    private boolean canSubmit;
    private Long createdAt;
    private Long updatedAt;
    private List<Long> propIds;
    private List<PropResponse> props;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Integer getUnavailableCount() { return unavailableCount; }
    public void setUnavailableCount(Integer unavailableCount) { this.unavailableCount = unavailableCount; }
    public boolean isCanSubmit() { return canSubmit; }
    public void setCanSubmit(boolean canSubmit) { this.canSubmit = canSubmit; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
    public List<Long> getPropIds() { return propIds; }
    public void setPropIds(List<Long> propIds) { this.propIds = propIds; }
    public List<PropResponse> getProps() { return props; }
    public void setProps(List<PropResponse> props) { this.props = props; }
}
