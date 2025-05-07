package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.AssignmentRequestDTO;
import com.filo.yazilimmuh_filo.entity.Employee;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDTO {
    private Long id;
    private String username;
    private String name;
    private String department;
    // Changed: using nested DTO objects instead of IDs
    private List<AssignmentRequestDTO> assignments;

    public EmployeeDTO() {}
    public EmployeeDTO(Employee e) {
        this.id = e.getId();
        this.username = e.getUsername();
        this.name = e.getName();
        this.department = e.getDepartment();
        this.assignments = e.getAssignments() != null ? e.getAssignments().stream().map(AssignmentRequestDTO::new).collect(Collectors.toList()) : null;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public List<AssignmentRequestDTO> getAssignments() { return assignments; }
    public void setAssignments(List<AssignmentRequestDTO> assignments) { this.assignments = assignments; }
}
