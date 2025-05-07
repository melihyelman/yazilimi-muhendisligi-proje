package com.filo.yazilimmuh_filo.entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee extends User{
    private String name;
    private String department;

    public Employee() {
        super();
        setRole(UserRole.EMPLOYEE);
    }

    public Employee(String username, String passwordHash, String department) {
        super(username, passwordHash, UserRole.EMPLOYEE);
        this.department = department;
    }

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeAssignment> assignments = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<EmployeeAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<EmployeeAssignment> assignments) {
        this.assignments = assignments;
    }

    public EmployeeAssignment assignVehicle(Vehicle v, LocalDate start, LocalDate end) {
        EmployeeAssignment ea = new EmployeeAssignment();
        ea.setEmployee(this);
        ea.setVehicle(v);
        ea.setStartDate(start);
        ea.setEndDate(end);
        assignments.add(ea);
        return ea;
    }
}
