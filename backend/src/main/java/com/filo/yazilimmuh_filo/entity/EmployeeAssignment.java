package com.filo.yazilimmuh_filo.entity;

import jakarta.persistence.*;
@Entity
public class EmployeeAssignment extends Assignment {
    private boolean approved;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
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
