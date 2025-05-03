package com.filo.yazilimmuh_filo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference(value = "vehicle-assignments")
    private Vehicle vehicle;

    @OneToMany(mappedBy="assignment", cascade=ALL)
    @JsonManagedReference("assignment-tripLogs")
    private List<TripLog> tripLogs = new ArrayList<>();

    public int calculateDuration() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public List<TripLog> getTripLogs() {
        return tripLogs;
    }

    public void setTripLogs(List<TripLog> tripLogs) {
        this.tripLogs = tripLogs;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}