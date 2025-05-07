package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.Expense;

import java.time.LocalDate;

public class ExpenseRequestDTO {
    private Long id;
    private LocalDate date;
    private String type;
    private Double amount;
    private String description;
    private Long vehicleId;
    private Long vendorId;
    private Long validatedById;

    public ExpenseRequestDTO() {}
    public ExpenseRequestDTO(Expense e) {
        this.id = e.getId();
        this.date = e.getDate();
        this.type = e.getType() != null ? e.getType().name() : null;
        this.amount = e.getAmount();
        this.description = e.getDescription();
        this.vehicleId = e.getVehicle() != null ? e.getVehicle().getId() : null;
        this.vendorId = e.getVendor() != null ? e.getVendor().getId() : null;
        this.validatedById = e.getValidatedBy() != null ? e.getValidatedBy().getId() : null;
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
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
    public Long getValidatedById() { return validatedById; }
    public void setValidatedById(Long validatedById) { this.validatedById = validatedById; }
}
