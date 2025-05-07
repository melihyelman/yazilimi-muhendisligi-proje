package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.TripLogRequestDTO;
import com.filo.yazilimmuh_filo.entity.Assignment;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private VehicleDTO vehicle;
    private List<TripLogRequestDTO> tripLogs;
    private String type;

    public AssignmentDTO() {}
    public AssignmentDTO(Assignment a) {
        this.id = a.getId();
        this.startDate = a.getStartDate();
        this.endDate = a.getEndDate();
        this.vehicle = a.getVehicle() != null ? new VehicleDTO(a.getVehicle()) : null;
        this.tripLogs = a.getTripLogs() != null ? a.getTripLogs().stream().map(TripLogRequestDTO::new).collect(Collectors.toList()) : null;
        this.type = a.getClass().getSimpleName();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public VehicleDTO getVehicle() { return vehicle; }
    public void setVehicle(VehicleDTO vehicle) { this.vehicle = vehicle; }
    public List<TripLogRequestDTO> getTripLogs() { return tripLogs; }
    public void setTripLogs(List<TripLogRequestDTO> tripLogs) { this.tripLogs = tripLogs; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
