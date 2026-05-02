package com.muhou.backend.infrastructure.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentalOrderEntity {

    private Long id;
    private String orderNo;
    private Long projectId;
    private Long demanderUserId;
    private Long supplierUserId;
    private String orderStatus;
    private Integer rentalDays;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private String contactAddress;
    private String useScene;
    private String specialRemark;
    private Integer rentAmountFen;
    private Integer depositAmountFen;
    private Integer totalAmountFen;
    private String payStatus;
    private String refundStatus;
    private Long currentPaymentId;
    private Integer totalPaidFen;
    private Integer totalRefundedFen;
    private LocalDateTime confirmDeadlineAt;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime returnedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime cancelledAt;
    private String cancelReason;
    private Integer depositRefundedFlag;
    private String remark;
    private Long createdBy;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getDemanderUserId() { return demanderUserId; }
    public void setDemanderUserId(Long demanderUserId) { this.demanderUserId = demanderUserId; }
    public Long getSupplierUserId() { return supplierUserId; }
    public void setSupplierUserId(Long supplierUserId) { this.supplierUserId = supplierUserId; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public Integer getRentalDays() { return rentalDays; }
    public void setRentalDays(Integer rentalDays) { this.rentalDays = rentalDays; }
    public LocalDate getRentalStartDate() { return rentalStartDate; }
    public void setRentalStartDate(LocalDate rentalStartDate) { this.rentalStartDate = rentalStartDate; }
    public LocalDate getRentalEndDate() { return rentalEndDate; }
    public void setRentalEndDate(LocalDate rentalEndDate) { this.rentalEndDate = rentalEndDate; }
    public String getContactAddress() { return contactAddress; }
    public void setContactAddress(String contactAddress) { this.contactAddress = contactAddress; }
    public String getUseScene() { return useScene; }
    public void setUseScene(String useScene) { this.useScene = useScene; }
    public String getSpecialRemark() { return specialRemark; }
    public void setSpecialRemark(String specialRemark) { this.specialRemark = specialRemark; }
    public Integer getRentAmountFen() { return rentAmountFen; }
    public void setRentAmountFen(Integer rentAmountFen) { this.rentAmountFen = rentAmountFen; }
    public Integer getDepositAmountFen() { return depositAmountFen; }
    public void setDepositAmountFen(Integer depositAmountFen) { this.depositAmountFen = depositAmountFen; }
    public Integer getTotalAmountFen() { return totalAmountFen; }
    public void setTotalAmountFen(Integer totalAmountFen) { this.totalAmountFen = totalAmountFen; }
    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }
    public String getRefundStatus() { return refundStatus; }
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }
    public Long getCurrentPaymentId() { return currentPaymentId; }
    public void setCurrentPaymentId(Long currentPaymentId) { this.currentPaymentId = currentPaymentId; }
    public Integer getTotalPaidFen() { return totalPaidFen; }
    public void setTotalPaidFen(Integer totalPaidFen) { this.totalPaidFen = totalPaidFen; }
    public Integer getTotalRefundedFen() { return totalRefundedFen; }
    public void setTotalRefundedFen(Integer totalRefundedFen) { this.totalRefundedFen = totalRefundedFen; }
    public LocalDateTime getConfirmDeadlineAt() { return confirmDeadlineAt; }
    public void setConfirmDeadlineAt(LocalDateTime confirmDeadlineAt) { this.confirmDeadlineAt = confirmDeadlineAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    public LocalDateTime getPickedUpAt() { return pickedUpAt; }
    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
    public void setReturnedAt(LocalDateTime returnedAt) { this.returnedAt = returnedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public Integer getDepositRefundedFlag() { return depositRefundedFlag; }
    public void setDepositRefundedFlag(Integer depositRefundedFlag) { this.depositRefundedFlag = depositRefundedFlag; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
