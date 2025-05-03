package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.OdometerReading;
import com.filo.yazilimmuh_filo.service.OdometerReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class OdometerReadingController {
    @Autowired
    private final OdometerReadingService readingService;

    public OdometerReadingController(OdometerReadingService readingService) {
        this.readingService = readingService;
    }

    @GetMapping
    public List<OdometerReading> getAll() {
        return readingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OdometerReading> getById(@PathVariable Long id) {
        return readingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OdometerReading create(@RequestBody OdometerReading reading) {
        return readingService.save(reading);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OdometerReading> update(@PathVariable Long id,
                                                  @RequestBody OdometerReading reading) {
        return readingService.findById(id).map(existing -> {
            existing.setDate(reading.getDate());
            existing.setKm(reading.getKm());
            existing.setVehicle(reading.getVehicle());
            readingService.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
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