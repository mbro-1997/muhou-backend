package com.muhou.backend.web.response;

public class AdminOverviewResponse {

    private int pendingFactoryCount;
    private int pendingPropCount;
    private int pendingDisputeCount;
    private int visibleReviewCount;
    private int totalUsers;

    public int getPendingFactoryCount() { return pendingFactoryCount; }
    public void setPendingFactoryCount(int pendingFactoryCount) { this.pendingFactoryCount = pendingFactoryCount; }
    public int getPendingPropCount() { return pendingPropCount; }
    public void setPendingPropCount(int pendingPropCount) { this.pendingPropCount = pendingPropCount; }
    public int getPendingDisputeCount() { return pendingDisputeCount; }
    public void setPendingDisputeCount(int pendingDisputeCount) { this.pendingDisputeCount = pendingDisputeCount; }
    public int getVisibleReviewCount() { return visibleReviewCount; }
    public void setVisibleReviewCount(int visibleReviewCount) { this.visibleReviewCount = visibleReviewCount; }
    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
}
