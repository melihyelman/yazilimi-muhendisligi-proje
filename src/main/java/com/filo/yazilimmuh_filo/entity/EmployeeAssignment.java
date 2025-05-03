package com.filo.yazilimmuh_filo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
@Entity
public class EmployeeAssignment extends Assignment {
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

    public boolean isApproved() {
        return approved;
    }

    public void approve() {
        setApproved(true);
    }

    public void reject() {
        setApproved(false);
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}