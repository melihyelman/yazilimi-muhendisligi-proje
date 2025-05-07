package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.VehicleRequestDTO;
import com.filo.yazilimmuh_filo.entity.PoolAssignment;

import java.time.LocalDate;

public class PoolAssignmentDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String mission;
    private VehicleRequestDTO vehicle;

    public PoolAssignmentDTO() {}
    public PoolAssignmentDTO(PoolAssignment ea) {
        this.id = ea.getId();
        this.startDate = ea.getStartDate();
        this.endDate = ea.getEndDate();
        this.mission = ea.getMission();
        this.vehicle = ea.getVehicle() != null ? new VehicleRequestDTO(ea.getVehicle()) : null;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getMission() { return mission; }
    public void setMission(String mission) { this.mission = mission; }
    public VehicleRequestDTO getVehicle() { return vehicle; }
    public void setVehicle(VehicleRequestDTO vehicle) { this.vehicle = vehicle;}
}
