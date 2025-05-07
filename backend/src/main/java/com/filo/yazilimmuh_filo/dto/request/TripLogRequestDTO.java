package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.TripLog;

import java.time.LocalDate;

public class TripLogRequestDTO {
    private Long id;
    private Long km;
    private Long assignmentId;

    public TripLogRequestDTO() {}
    public TripLogRequestDTO(TripLog t) {
        this.id = t.getId();
        this.km = t.getKm();
        this.assignmentId = t.getAssignment() != null ? t.getAssignment().getId() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getKm() { return km; }
    public void setKm(Long km) { this.km = km; }
    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
}
