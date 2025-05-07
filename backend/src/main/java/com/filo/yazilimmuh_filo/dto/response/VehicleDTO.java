package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.AssignmentRequestDTO;
import com.filo.yazilimmuh_filo.dto.request.ExpenseRequestDTO;
import com.filo.yazilimmuh_filo.dto.request.OdometerReadingRequestDTO;
import com.filo.yazilimmuh_filo.entity.Vehicle;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleDTO {
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private String ownership;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private List<AssignmentRequestDTO> assignments;
    private List<OdometerReadingRequestDTO> readings;
    private List<ExpenseRequestDTO> expenses;

    public VehicleDTO() {}

    public VehicleDTO(Vehicle v) {
        this.id = v.getId();
        this.plateNumber = v.getPlateNumber();
        this.brand = v.getBrand();
        this.model = v.getModel();
        this.ownership = v.getOwnership() != null ? v.getOwnership().name() : null;
        this.leaseStartDate = v.getLeaseStartDate();
        this.leaseEndDate = v.getLeaseEndDate();
        this.assignments = v.getAssignments() != null ? v.getAssignments().stream().map(AssignmentRequestDTO::new).collect(Collectors.toList()) : null;
        this.readings = v.getReadings() != null ? v.getReadings().stream().map(OdometerReadingRequestDTO::new).collect(Collectors.toList()) : null;
        this.expenses = v.getExpenses() != null ? v.getExpenses().stream().map(ExpenseRequestDTO::new).collect(Collectors.toList()) : null;
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
    public List<AssignmentRequestDTO> getAssignments() { return assignments; }
    public void setAssignments(List<AssignmentRequestDTO> assignments) { this.assignments = assignments; }
    public List<OdometerReadingRequestDTO> getReadings() { return readings; }
    public void setReadings(List<OdometerReadingRequestDTO> readings) { this.readings = readings; }
    public List<ExpenseRequestDTO> getExpenses() { return expenses; }
    public void setExpenses(List<ExpenseRequestDTO> expenses) { this.expenses = expenses; }

    public static VehicleDTO fromEntity(Vehicle v) {
        return new VehicleDTO(v);
    }
}
