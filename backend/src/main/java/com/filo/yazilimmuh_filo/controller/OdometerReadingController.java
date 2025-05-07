package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.OdometerReadingDTO;
import com.filo.yazilimmuh_filo.entity.OdometerReading;
import com.filo.yazilimmuh_filo.service.OdometerReadingService;
import com.filo.yazilimmuh_filo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/readings")
public class OdometerReadingController {
    @Autowired
    private OdometerReadingService readingService;
    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public List<OdometerReadingDTO> getAll() {
        return readingService.findAll().stream().map(OdometerReadingDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OdometerReadingDTO> getById(@PathVariable Long id) {
        return readingService.findById(id)
                .map(o -> ResponseEntity.ok(new OdometerReadingDTO(o)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OdometerReadingDTO create(@RequestBody Map<String, Object> payload) {
        OdometerReading reading = new OdometerReading();
        reading.setDate(LocalDate.parse((String) payload.get("date")));
        reading.setKm((Integer) payload.get("km"));
        if (payload.get("vehicle_id") != null) {
            Long vehicleId = Long.valueOf(payload.get("vehicle_id").toString());
            vehicleService.findById(vehicleId).ifPresent(reading::setVehicle);
        }
        return new OdometerReadingDTO(readingService.save(reading));
    }
    @PutMapping("/{id}")
    public ResponseEntity<OdometerReadingDTO> update(@PathVariable Long id,
                                                  @RequestBody OdometerReading reading) {
        return readingService.findById(id).map(existing -> {
            if (reading.getDate() != null) existing.setDate(reading.getDate());
            if (reading.getVehicle() != null) existing.setVehicle(reading.getVehicle());
            if (reading.getKm() != 0) existing.setKm(reading.getKm());
            return ResponseEntity.ok(new OdometerReadingDTO(readingService.save(existing)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<OdometerReadingDTO>> listByVehicle(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(readingService.findByVehicleId(vehicleId).stream().map(OdometerReadingDTO::new).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (readingService.findById(id).isPresent()) {
            readingService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}