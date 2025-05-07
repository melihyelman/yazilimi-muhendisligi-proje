package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.VehicleRequestDTO;
import com.filo.yazilimmuh_filo.dto.request.VendorRequestDTO;
import com.filo.yazilimmuh_filo.entity.Expense;
import java.time.LocalDate;

public class ExpenseDTO {
    private Long id;
    private LocalDate date;
    private String type;
    private Double amount;
    private String description;
    // Changed: using nested DTO objects instead of IDs
    private VehicleRequestDTO vehicle;
    private VendorRequestDTO vendor;
    private UserResponse validatedBy;

    public ExpenseDTO() {}
    public ExpenseDTO(Expense e) {
        this.id = e.getId();
        this.date = e.getDate();
        this.type = e.getType() != null ? e.getType().name() : null;
        this.amount = e.getAmount();
        this.description = e.getDescription();
        this.vehicle = e.getVehicle() != null ? new VehicleRequestDTO(e.getVehicle()) : null;
        this.vendor = e.getVendor() != null ? new VendorRequestDTO(e.getVendor()) : null;
        this.validatedBy = e.getValidatedBy() != null ? new UserResponse(e.getValidatedBy()) : null;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public VehicleRequestDTO getVehicle() { return vehicle; }
    public void setVehicle(VehicleRequestDTO vehicle) { this.vehicle = vehicle; }
    public VendorRequestDTO getVendor() { return vendor; }
    public void setVendor(VendorRequestDTO vendor) { this.vendor = vendor; }
    public UserResponse getValidatedBy() { return validatedBy; }
    public void setValidatedBy(UserResponse validatedBy) { this.validatedBy = validatedBy; }
}
