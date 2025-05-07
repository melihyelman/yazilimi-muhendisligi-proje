package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.OdometerReading;

import java.time.LocalDate;

public class OdometerReadingRequestDTO {
    private Long id;
    private LocalDate date;
    private int km;
    private Long vehicleId;

    public OdometerReadingRequestDTO() {}
    public OdometerReadingRequestDTO(OdometerReading o) {
        this.id = o.getId();
        this.date = o.getDate();
        this.km = o.getKm();
        this.vehicleId = o.getVehicle() != null ? o.getVehicle().getId() : null;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getKm() { return km; }
    public void setKm(int km) { this.km = km; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
}
