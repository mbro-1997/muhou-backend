package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CreateOrderRequest {

    @NotNull(message = "projectId is required")
    private Long projectId;

    private Integer rentalDays;

    @NotNull(message = "rentalStartDate is required")
    private LocalDate rentalStartDate;

    @NotNull(message = "rentalEndDate is required")
    private LocalDate rentalEndDate;

    @NotBlank(message = "contactAddress is required")
    @Size(max = 255, message = "contactAddress must not exceed 255 chars")
    private String contactAddress;

    @NotBlank(message = "useScene is required")
    @Size(max = 100, message = "useScene must not exceed 100 chars")
    private String useScene;

    @Size(max = 500, message = "specialRemark must not exceed 500 chars")
    private String specialRemark;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(Integer rentalDays) {
        this.rentalDays = rentalDays;
    }

    public LocalDate getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDate rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDate getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(LocalDate rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getUseScene() {
        return useScene;
    }

    public void setUseScene(String useScene) {
        this.useScene = useScene;
    }

    public String getSpecialRemark() {
        return specialRemark;
    }

    public void setSpecialRemark(String specialRemark) {
        this.specialRemark = specialRemark;
    }
}
