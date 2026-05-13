package com.muhou.backend.web.response;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {

    private Long id;
    private String orderNo;
    private Long projectId;
    private String status;
    private String statusText;
    private String payStatus;
    private Long supplierUserId;
    private String supplierName;
    private Long paymentId;
    private String paymentNo;
    private String sourceProjectName;
    private Integer rentalDays;
    private String rentalStartDate;
    private String rentalEndDate;
    private String contactAddress;
    private String useScene;
    private String specialRemark;
    private String cancelReason;
    private String demanderName;
    private String demanderPhone;
    private String demanderIdentity;
    private Integer demanderCreditScore;
    private BigDecimal price;
    private BigDecimal deposit;
    private String date;
    private List<Long> propIds;
    private List<OrderItemResponse> props;
    private int propCount;
    private int outboundScannedCount;
    private int returnScannedCount;
    private boolean canFactoryConfirm;
    private boolean canFactoryReject;
    private boolean canScanOutbound;
    private boolean canScanReturn;
    private boolean canReview;
    private boolean canApplyDispute;
    private int disputeCount;
    private boolean demanderReviewed;
    private boolean supplierReviewed;
    private Long confirmDeadlineAt;
    private Long createdAt;
    private Long paidAt;
    private Long confirmedAt;
    private Long pickedUpAt;
    private Long returnedAt;
    private Long reviewedAt;
    private List<DisputeResponse> disputes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }
    public Long getSupplierUserId() { return supplierUserId; }
    public void setSupplierUserId(Long supplierUserId) { this.supplierUserId = supplierUserId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public String getPaymentNo() { return paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    public String getSourceProjectName() { return sourceProjectName; }
    public void setSourceProjectName(String sourceProjectName) { this.sourceProjectName = sourceProjectName; }
    public Integer getRentalDays() { return rentalDays; }
    public void setRentalDays(Integer rentalDays) { this.rentalDays = rentalDays; }
    public String getRentalStartDate() { return rentalStartDate; }
    public void setRentalStartDate(String rentalStartDate) { this.rentalStartDate = rentalStartDate; }
    public String getRentalEndDate() { return rentalEndDate; }
    public void setRentalEndDate(String rentalEndDate) { this.rentalEndDate = rentalEndDate; }
    public String getContactAddress() { return contactAddress; }
    public void setContactAddress(String contactAddress) { this.contactAddress = contactAddress; }
    public String getUseScene() { return useScene; }
    public void setUseScene(String useScene) { this.useScene = useScene; }
    public String getSpecialRemark() { return specialRemark; }
    public void setSpecialRemark(String specialRemark) { this.specialRemark = specialRemark; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getDemanderName() { return demanderName; }
    public void setDemanderName(String demanderName) { this.demanderName = demanderName; }
    public String getDemanderPhone() { return demanderPhone; }
    public void setDemanderPhone(String demanderPhone) { this.demanderPhone = demanderPhone; }
    public String getDemanderIdentity() { return demanderIdentity; }
    public void setDemanderIdentity(String demanderIdentity) { this.demanderIdentity = demanderIdentity; }
    public Integer getDemanderCreditScore() { return demanderCreditScore; }
    public void setDemanderCreditScore(Integer demanderCreditScore) { this.demanderCreditScore = demanderCreditScore; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getDeposit() { return deposit; }
    public void setDeposit(BigDecimal deposit) { this.deposit = deposit; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<Long> getPropIds() { return propIds; }
    public void setPropIds(List<Long> propIds) { this.propIds = propIds; }
    public List<OrderItemResponse> getProps() { return props; }
    public void setProps(List<OrderItemResponse> props) { this.props = props; }
    public int getPropCount() { return propCount; }
    public void setPropCount(int propCount) { this.propCount = propCount; }
    public int getOutboundScannedCount() { return outboundScannedCount; }
    public void setOutboundScannedCount(int outboundScannedCount) { this.outboundScannedCount = outboundScannedCount; }
    public int getReturnScannedCount() { return returnScannedCount; }
    public void setReturnScannedCount(int returnScannedCount) { this.returnScannedCount = returnScannedCount; }
    public boolean isCanFactoryConfirm() { return canFactoryConfirm; }
    public void setCanFactoryConfirm(boolean canFactoryConfirm) { this.canFactoryConfirm = canFactoryConfirm; }
    public boolean isCanFactoryReject() { return canFactoryReject; }
    public void setCanFactoryReject(boolean canFactoryReject) { this.canFactoryReject = canFactoryReject; }
    public boolean isCanScanOutbound() { return canScanOutbound; }
    public void setCanScanOutbound(boolean canScanOutbound) { this.canScanOutbound = canScanOutbound; }
    public boolean isCanScanReturn() { return canScanReturn; }
    public void setCanScanReturn(boolean canScanReturn) { this.canScanReturn = canScanReturn; }
    public boolean isCanReview() { return canReview; }
    public void setCanReview(boolean canReview) { this.canReview = canReview; }
    public boolean isCanApplyDispute() { return canApplyDispute; }
    public void setCanApplyDispute(boolean canApplyDispute) { this.canApplyDispute = canApplyDispute; }
    public int getDisputeCount() { return disputeCount; }
    public void setDisputeCount(int disputeCount) { this.disputeCount = disputeCount; }
    public boolean isDemanderReviewed() { return demanderReviewed; }
    public void setDemanderReviewed(boolean demanderReviewed) { this.demanderReviewed = demanderReviewed; }
    public boolean isSupplierReviewed() { return supplierReviewed; }
    public void setSupplierReviewed(boolean supplierReviewed) { this.supplierReviewed = supplierReviewed; }
    public Long getConfirmDeadlineAt() { return confirmDeadlineAt; }
    public void setConfirmDeadlineAt(Long confirmDeadlineAt) { this.confirmDeadlineAt = confirmDeadlineAt; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    public Long getPaidAt() { return paidAt; }
    public void setPaidAt(Long paidAt) { this.paidAt = paidAt; }
    public Long getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(Long confirmedAt) { this.confirmedAt = confirmedAt; }
    public Long getPickedUpAt() { return pickedUpAt; }
    public void setPickedUpAt(Long pickedUpAt) { this.pickedUpAt = pickedUpAt; }
    public Long getReturnedAt() { return returnedAt; }
    public void setReturnedAt(Long returnedAt) { this.returnedAt = returnedAt; }
    public Long getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Long reviewedAt) { this.reviewedAt = reviewedAt; }
    public List<DisputeResponse> getDisputes() { return disputes; }
    public void setDisputes(List<DisputeResponse> disputes) { this.disputes = disputes; }
}
