package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.Assignment;
import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.OdometerReading;
import com.filo.yazilimmuh_filo.entity.Vehicle;
import com.filo.yazilimmuh_filo.service.AssignmentService;
import com.filo.yazilimmuh_filo.service.ExpenseService;
import com.filo.yazilimmuh_filo.service.OdometerReadingService;
import com.filo.yazilimmuh_filo.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final OdometerReadingService readingService;
    private final AssignmentService assignmentService;
    private final ExpenseService expenseService;

    public VehicleController(VehicleService vehicleService,
                             OdometerReadingService readingService,
                             AssignmentService assignmentService,
                             ExpenseService expenseService) {
        this.vehicleService = vehicleService;
        this.readingService = readingService;
        this.assignmentService = assignmentService;
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Vehicle> getAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return vehicleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle create(@RequestBody Vehicle vehicle) {
        System.out.println("Creating vehicle: " + vehicle.getModel());
        return vehicleService.save(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id,
                                          @RequestBody Vehicle vehicle) {
        return vehicleService.findById(id).map(existing -> {
            existing.setPlateNumber(vehicle.getPlateNumber());
            existing.setBrand(vehicle.getBrand());
            existing.setModel(vehicle.getModel());
            existing.setOwnership(vehicle.getOwnership());
            existing.setLeaseStartDate(vehicle.getLeaseStartDate());
            existing.setLeaseEndDate(vehicle.getLeaseEndDate());
            existing.setReadings(vehicle.getReadings());
            existing.setExpenses(vehicle.getExpenses());
            existing.setAssignments(vehicle.getAssignments());
            vehicleService.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (vehicleService.findById(id).isPresent()) {
            vehicleService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/current-odo")
    public int currentOdo(@PathVariable Long id) {
        return vehicleService.getCurrentOdometer(id);
    }

    @GetMapping("/{id}/mileage")
    public int mileage(@PathVariable Long id,
                       @RequestParam LocalDate from,
                       @RequestParam LocalDate to) {
        return vehicleService.getMileage(id, from, to);
    }

    @GetMapping("/{id}/readings")
    public List<OdometerReading> getReadings(@PathVariable Long id) {
        return readingService.findByVehicleId(id);
    }

    @GetMapping("/{id}/expenses")
    public List<Expense> getExpenses(@PathVariable Long id) {
        return expenseService.findByVehicleId(id);
    }

    @GetMapping("/{id}/assignments")
    public List<Assignment> getAssignments(@PathVariable Long id) {
        return assignmentService.findByVehicleId(id);
    }
}