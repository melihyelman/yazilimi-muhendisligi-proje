package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeRequestDTO {
    private Long id;
    private String username;
    private String name;
    private String department;
    // Changed: using nested DTO objects instead of IDs
    private List<Long> assignmentsIds;

    public EmployeeRequestDTO() {}
    public EmployeeRequestDTO(Employee e) {
        this.id = e.getId();
        this.username = e.getUsername();
        this.name = e.getName();
        this.department = e.getDepartment();
        this.assignmentsIds = e.getAssignments() != null ? e.getAssignments().stream().map(a -> a.getId()).collect(Collectors.toList()) : null;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public List<Long> getAssignmentsIds() { return assignmentsIds; }
    public void setAssignmentsIds(List<Long> assignmentsIds) { this.assignmentsIds = assignmentsIds; }
}
