package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.EmployeeRequestDTO;
import com.filo.yazilimmuh_filo.dto.request.VehicleRequestDTO;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import java.time.LocalDate;

public class EmployeeAssignmentDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;
    private EmployeeRequestDTO employee;
    private VehicleRequestDTO vehicle;

    public EmployeeAssignmentDTO() {}
    public EmployeeAssignmentDTO(EmployeeAssignment ea) {
        this.id = ea.getId();
        this.startDate = ea.getStartDate();
        this.endDate = ea.getEndDate();
        this.approved = ea.isApproved();
        this.employee = ea.getEmployee() != null ? new EmployeeRequestDTO(ea.getEmployee()) : null;
        this.vehicle = ea.getVehicle() != null ? new VehicleRequestDTO(ea.getVehicle()) : null;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public EmployeeRequestDTO getEmployee() { return employee; }
    public void setEmployee(EmployeeRequestDTO employee) { this.employee = employee; }
    public VehicleRequestDTO getVehicle() { return vehicle; }
    public void setVehicle(VehicleRequestDTO vehicle) { this.vehicle = vehicle; }
}
