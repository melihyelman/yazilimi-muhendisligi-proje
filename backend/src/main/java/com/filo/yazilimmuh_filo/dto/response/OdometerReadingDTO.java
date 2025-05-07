package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.VehicleRequestDTO;
import com.filo.yazilimmuh_filo.entity.OdometerReading;
import java.time.LocalDate;

public class OdometerReadingDTO {
    private Long id;
    private LocalDate date;
    private int km;
    private VehicleRequestDTO vehicle;

    public OdometerReadingDTO() {}
    public OdometerReadingDTO(OdometerReading o) {
        this.id = o.getId();
        this.date = o.getDate();
        this.km = o.getKm();
        this.vehicle = o.getVehicle() != null ? new VehicleRequestDTO(o.getVehicle()) : null;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getKm() { return km; }
    public void setKm(int km) { this.km = km; }
    public VehicleRequestDTO getVehicle() { return vehicle; }
    public void setVehicle(VehicleRequestDTO vehicle) { this.vehicle = vehicle; }
}
