package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.AssignmentRequestDTO;
import com.filo.yazilimmuh_filo.dto.request.EmployeeAssignmentRequestDTO;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.entity.TripLog;
import java.time.LocalDate;

public class TripLogDTO {
    private Long id;
    private Long km;
    // Changed: using nested DTO instead of id
    private EmployeeAssignmentRequestDTO assignment;

    public TripLogDTO() {}
    public TripLogDTO(TripLog t) {
        this.id = t.getId();
        this.km = t.getKm();
        this.assignment = t.getAssignment() != null ? new EmployeeAssignmentRequestDTO((EmployeeAssignment) t.getAssignment()) : null;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getKm() { return km; }
    public void setKm(Long km) { this.km = km; }
    public EmployeeAssignmentRequestDTO getAssignment() { return assignment; }
    public void setAssignment(EmployeeAssignmentRequestDTO assignment) { this.assignment = assignment; }
}
