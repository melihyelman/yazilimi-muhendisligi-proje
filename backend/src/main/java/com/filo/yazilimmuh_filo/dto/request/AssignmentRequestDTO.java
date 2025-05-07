package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.Assignment;
import com.filo.yazilimmuh_filo.entity.TripLog;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentRequestDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long vehicleId;
    private List<Long> tripLogIds;
    private String type;

    public AssignmentRequestDTO() {}
    public AssignmentRequestDTO(Assignment a) {
        this.id = a.getId();
        this.startDate = a.getStartDate();
        this.endDate = a.getEndDate();
        this.vehicleId = a.getVehicle() != null ? a.getVehicle().getId() : null;
        this.tripLogIds = a.getTripLogs() != null ? a.getTripLogs().stream().map(TripLog::getId).collect(Collectors.toList()) : null;
        this.type = a.getClass().getSimpleName();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public List<Long> getTripLogIds() { return tripLogIds; }
    public void setTripLogIds(List<Long> tripLogIds) { this.tripLogIds = tripLogIds; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
