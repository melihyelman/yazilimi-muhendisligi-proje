package com.filo.yazilimmuh_filo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    @Enumerated(EnumType.STRING)
    private OwnershipType ownership;

    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "vehicle-readings")
    private List<OdometerReading> readings = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "vehicle-assignments")
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "vehicle-expenses")
    private List<Expense> expenses = new ArrayList<>();

    public void addOdometerReading(OdometerReading r) {
        r.setVehicle(this);
        readings.add(r);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<OdometerReading> getReadings() {
        return readings;
    }

    public void setReadings(List<OdometerReading> readings) {
        this.readings = readings;
    }

    public OwnershipType getOwnership() {
        return ownership;
    }

    public void setOwnership(OwnershipType ownership) {
        this.ownership = ownership;
    }

    public LocalDate getLeaseStartDate() {
        return leaseStartDate;
    }

    public void setLeaseStartDate(LocalDate leaseStartDate) {
        this.leaseStartDate = leaseStartDate;
    }

    public LocalDate getLeaseEndDate() {
        return leaseEndDate;
    }

    public void setLeaseEndDate(LocalDate leaseEndDate) {
        this.leaseEndDate = leaseEndDate;
    }

    public PoolAssignment assignToPool(String mission) {
        PoolAssignment pa = new PoolAssignment();
        pa.setMission(mission);
        pa.setVehicle(this);
        this.assignments.add(pa);
        return pa;
    }

    public EmployeeAssignment assignToEmployee(Employee emp, LocalDate start, LocalDate end) {
        EmployeeAssignment ea = new EmployeeAssignment();
        ea.setEmployee(emp);
        ea.setStartDate(start);
        ea.setEndDate(end);
        ea.setVehicle(this);
        this.assignments.add(ea);
        return ea;
    }

    public int getCurrentOdometer() {
        return readings.stream()
                .max((a, b) -> a.getDate().compareTo(b.getDate()))
                .map(OdometerReading::getKm)
                .orElse(0);
    }

    public int getMileage(LocalDate from, LocalDate to) {
        List<OdometerReading> filtered = readings.stream()
                .filter(r -> !r.getDate().isBefore(from) && !r.getDate().isAfter(to))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
        if (filtered.size() < 2) return 0;
        return filtered.get(filtered.size()-1).getKm() - filtered.get(0).getKm();
    }
}
