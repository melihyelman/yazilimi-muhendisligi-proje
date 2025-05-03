package com.filo.yazilimmuh_filo.entity;

import jakarta.persistence.Entity;

@Entity
public class PoolAssignment extends Assignment {
    private String mission;

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }
}