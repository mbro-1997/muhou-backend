package com.muhou.backend.web.response;

public class PropInstanceResponse {

    private Long id;
    private Long propId;
    private String instanceNo;
    private String qrCodeId;
    private String instanceStatus;
    private String instanceStatusText;
    private Long currentOrderId;
    private String currentOrderNo;
    private String statusRemark;
    private String statusChangedAt;
    private Long statusChangedBy;
    private boolean canMarkRepairing;
    private boolean canMarkLost;
    private boolean canMarkScrapped;
    private boolean canRestoreIdle;
    private boolean canScrapFromRepairing;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropId() { return propId; }
    public void setPropId(Long propId) { this.propId = propId; }
    public String getInstanceNo() { return instanceNo; }
    public void setInstanceNo(String instanceNo) { this.instanceNo = instanceNo; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
    public String getInstanceStatus() { return instanceStatus; }
    public void setInstanceStatus(String instanceStatus) { this.instanceStatus = instanceStatus; }
    public String getInstanceStatusText() { return instanceStatusText; }
    public void setInstanceStatusText(String instanceStatusText) { this.instanceStatusText = instanceStatusText; }
    public Long getCurrentOrderId() { return currentOrderId; }
    public void setCurrentOrderId(Long currentOrderId) { this.currentOrderId = currentOrderId; }
    public String getCurrentOrderNo() { return currentOrderNo; }
    public void setCurrentOrderNo(String currentOrderNo) { this.currentOrderNo = currentOrderNo; }
    public String getStatusRemark() { return statusRemark; }
    public void setStatusRemark(String statusRemark) { this.statusRemark = statusRemark; }
    public String getStatusChangedAt() { return statusChangedAt; }
    public void setStatusChangedAt(String statusChangedAt) { this.statusChangedAt = statusChangedAt; }
    public Long getStatusChangedBy() { return statusChangedBy; }
    public void setStatusChangedBy(Long statusChangedBy) { this.statusChangedBy = statusChangedBy; }
    public boolean isCanMarkRepairing() { return canMarkRepairing; }
    public void setCanMarkRepairing(boolean canMarkRepairing) { this.canMarkRepairing = canMarkRepairing; }
    public boolean isCanMarkLost() { return canMarkLost; }
    public void setCanMarkLost(boolean canMarkLost) { this.canMarkLost = canMarkLost; }
    public boolean isCanMarkScrapped() { return canMarkScrapped; }
    public void setCanMarkScrapped(boolean canMarkScrapped) { this.canMarkScrapped = canMarkScrapped; }
    public boolean isCanRestoreIdle() { return canRestoreIdle; }
    public void setCanRestoreIdle(boolean canRestoreIdle) { this.canRestoreIdle = canRestoreIdle; }
    public boolean isCanScrapFromRepairing() { return canScrapFromRepairing; }
    public void setCanScrapFromRepairing(boolean canScrapFromRepairing) { this.canScrapFromRepairing = canScrapFromRepairing; }
}
