package com.filo.yazilimmuh_filo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class TripLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int startKm;
    private int endKm;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    @JsonBackReference("assignment-tripLogs")
    private Assignment assignment;

    public int getDistance() {
        return endKm - startKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getStartKm() {
        return startKm;
    }

    public void setStartKm(int startKm) {
        this.startKm = startKm;
    }

    public int getEndKm() {
        return endKm;
    }

    public void setEndKm(int endKm) {
        this.endKm = endKm;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
