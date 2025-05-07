package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.Vehicle;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleRequestDTO {
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private String ownership;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private List<Long> assignmentIds;
    private List<Long> readingIds;
    private List<Long> expenseIds;

    public VehicleRequestDTO() {}

    public VehicleRequestDTO(Vehicle v) {
        this.id = v.getId();
        this.plateNumber = v.getPlateNumber();
        this.brand = v.getBrand();
        this.model = v.getModel();
        this.ownership = v.getOwnership() != null ? v.getOwnership().name() : null;
        this.leaseStartDate = v.getLeaseStartDate();
        this.leaseEndDate = v.getLeaseEndDate();
        this.assignmentIds = v.getAssignments() != null ? v.getAssignments().stream().map(a -> a.getId()).collect(Collectors.toList()) : null;
        this.readingIds = v.getReadings() != null ? v.getReadings().stream().map(r -> r.getId()).collect(Collectors.toList()) : null;
        this.expenseIds = v.getExpenses() != null ? v.getExpenses().stream().map(e -> e.getId()).collect(Collectors.toList()) : null;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getOwnership() { return ownership; }
    public void setOwnership(String ownership) { this.ownership = ownership; }
    public LocalDate getLeaseStartDate() { return leaseStartDate; }
    public void setLeaseStartDate(LocalDate leaseStartDate) { this.leaseStartDate = leaseStartDate; }
    public LocalDate getLeaseEndDate() { return leaseEndDate; }
    public void setLeaseEndDate(LocalDate leaseEndDate) { this.leaseEndDate = leaseEndDate; }
    public List<Long> getAssignmentIds() { return assignmentIds; }
    public void setAssignmentIds(List<Long> assignmentIds) { this.assignmentIds = assignmentIds; }
    public List<Long> getReadingIds() { return readingIds; }
    public void setReadingIds(List<Long> readingIds) { this.readingIds = readingIds; }
    public List<Long> getExpenseIds() { return expenseIds; }
    public void setExpenseIds(List<Long> expenseIds) { this.expenseIds = expenseIds; }

    public static VehicleRequestDTO fromEntity(Vehicle v) {
        return new VehicleRequestDTO(v);
    }
}
