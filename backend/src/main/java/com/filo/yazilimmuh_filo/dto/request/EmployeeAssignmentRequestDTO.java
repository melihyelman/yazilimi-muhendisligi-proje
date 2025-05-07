package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;

import java.time.LocalDate;

public class EmployeeAssignmentRequestDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;
    private Long employeeId;
    private Long vehicleId;

    public EmployeeAssignmentRequestDTO() {}
    public EmployeeAssignmentRequestDTO(EmployeeAssignment ea) {
        this.id = ea.getId();
        this.startDate = ea.getStartDate();
        this.endDate = ea.getEndDate();
        this.approved = ea.isApproved();
        this.employeeId = ea.getEmployee() != null ? ea.getEmployee().getId() : null;
        this.vehicleId = ea.getVehicle() != null ? ea.getVehicle().getId() : null;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
}
