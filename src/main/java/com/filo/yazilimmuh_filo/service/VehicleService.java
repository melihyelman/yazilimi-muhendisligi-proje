package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Vehicle;
import com.filo.yazilimmuh_filo.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.filo.yazilimmuh_filo.entity.OdometerReading;

@Service
@Transactional
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final OdometerReadingService readingService;

    public VehicleService(VehicleRepository vehicleRepository, OdometerReadingService readingService) {
        this.vehicleRepository = vehicleRepository;
        this.readingService = readingService;
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }

    public int getCurrentOdometer(Long vehicleId) {
        var readings = readingService.findByVehicleId(vehicleId);
        return readings.stream()
                .max(Comparator.comparing(OdometerReading::getDate))
                .map(OdometerReading::getKm)
                .orElse(0);
    }

    public int getMileage(Long vehicleId, LocalDate from, LocalDate to) {
        var list = readingService.findByVehicleId(vehicleId).stream()
                .filter(r -> !r.getDate().isBefore(from) && !r.getDate().isAfter(to))
                .sorted(Comparator.comparing(OdometerReading::getDate))
                .toList();
        if (list.size()<2) return 0;
        return list.get(list.size()-1).getKm() - list.get(0).getKm();
    }
}