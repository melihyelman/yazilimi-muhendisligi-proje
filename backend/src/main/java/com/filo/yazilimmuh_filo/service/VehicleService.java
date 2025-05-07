package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.*;
import com.filo.yazilimmuh_filo.repository.EmployeeRepository;
import com.filo.yazilimmuh_filo.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private OdometerReadingService readingService;
    @Autowired
    private EmployeeRepository employeeRepo;


    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Long id, Vehicle updated) {
        Vehicle v = findById(id).get();
        v.setPlateNumber(updated.getPlateNumber());
        v.setModel(updated.getModel());
        v.setBrand(updated.getBrand());
        v.setOwnership(updated.getOwnership());
        v.setLeaseStartDate(updated.getLeaseStartDate());
        v.setLeaseEndDate(updated.getLeaseEndDate());
        return vehicleRepository.save(v);
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

    public int getMileage(Long vehicleId, LocalDate start, LocalDate end) {
        return findById(vehicleId).get().getMileage(start, end);
    }

    public PoolAssignment assignToPool(Long vehicleId, String mission,
                                       LocalDate start,
                                       LocalDate end) {
        Vehicle v = findById(vehicleId).get();
        PoolAssignment p = v.assignToPool(mission);
        p.setStartDate(start);
        p.setEndDate(end);
        p.setVehicle(v);
        v.getAssignments().add(p);
        vehicleRepository.save(v);
        return p;
    }

    public EmployeeAssignment assignToEmployee(Long vehicleId,
                                               Long employeeId,
                                               LocalDate start,
                                               LocalDate end) {
        Vehicle v = findById(vehicleId).get();
        Employee e = employeeRepo.findById(employeeId).get();
        EmployeeAssignment ea = v.assignToEmployee(e, start, end);
        e.setAssignments(List.of(ea));
        ea.setVehicle(v);
        ea.setEmployee(e);
        ea.setStartDate(start);
        ea.setEndDate(end);
        vehicleRepository.save(v);
        return ea;
    }
}