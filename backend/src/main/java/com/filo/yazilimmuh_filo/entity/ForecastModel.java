package com.filo.yazilimmuh_filo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ForecastModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int windowSize;

    @OneToOne(mappedBy = "forecastModel", cascade = CascadeType.ALL)
    private Vehicle vehicle;

    public ForecastModel() {}
    public ForecastModel(int windowSize) { this.windowSize = windowSize; }

    public double calculateMovingAverage(List<Double> data) {
        if (windowSize <= 0 || data.size() < windowSize) {
            throw new IllegalArgumentException("Invalid window size");
        }
        int start = data.size() - windowSize;
        return data.subList(start, data.size())
                .stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}