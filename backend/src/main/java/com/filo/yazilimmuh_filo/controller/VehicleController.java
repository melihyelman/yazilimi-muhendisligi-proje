package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.EmployeeAssignmentDTO;
import com.filo.yazilimmuh_filo.dto.response.PoolAssignmentDTO;
import com.filo.yazilimmuh_filo.dto.response.VehicleDTO;
import com.filo.yazilimmuh_filo.entity.*;
import com.filo.yazilimmuh_filo.repository.UserRepository;
import com.filo.yazilimmuh_filo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public List<VehicleDTO> getAll() {
        return vehicleService.findAll().stream().map(VehicleDTO::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getById(@PathVariable Long id) {
        return vehicleService.findById(id)
                .map(v -> ResponseEntity.ok(VehicleDTO.fromEntity(v)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public VehicleDTO create(@RequestBody Vehicle vehicle) {
        return new VehicleDTO(vehicleService.save(vehicle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable Long id,
                                          @RequestBody Vehicle vehicle) {
        return vehicleService.findById(id).map(existing -> {
            if (vehicle.getPlateNumber() != null) existing.setPlateNumber(vehicle.getPlateNumber());
            if (vehicle.getBrand() != null) existing.setBrand(vehicle.getBrand());
            if (vehicle.getModel() != null) existing.setModel(vehicle.getModel());
            if (vehicle.getOwnership() != null) existing.setOwnership(vehicle.getOwnership());
            if (vehicle.getLeaseStartDate() != null) existing.setLeaseStartDate(vehicle.getLeaseStartDate());
            if (vehicle.getLeaseEndDate() != null) existing.setLeaseEndDate(vehicle.getLeaseEndDate());
            vehicleService.save(existing);
            return ResponseEntity.ok(new VehicleDTO(existing));
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

    @GetMapping("/{id}/odometer")
    public ResponseEntity<Map<String,Integer>> mileage(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        int km = vehicleService.getMileage(id, start, end);
        return ResponseEntity.ok(Map.of("mileage", km));
    }

    @PostMapping("/{id}/assign/pool")
    public ResponseEntity<PoolAssignmentDTO> assignPool(
            @PathVariable Long id,
            @RequestBody Map<String,String> payload) {

        LocalDate start = LocalDate.parse(payload.get("startDate"));
        LocalDate end   = LocalDate.parse(payload.get("endDate"));
        String mission = payload.get("mission");
        PoolAssignment p = vehicleService.assignToPool(id, mission, start, end);
        return ResponseEntity.ok(new PoolAssignmentDTO(p));
    }

    @PostMapping("/{id}/assign/employee")
    public ResponseEntity<EmployeeAssignmentDTO> assignEmployee(
            @PathVariable Long id,
            @RequestBody Map<String,String> payload) {

        Long empId = Long.valueOf(payload.get("employeeId"));
        LocalDate start = LocalDate.parse(payload.get("startDate"));
        LocalDate end   = LocalDate.parse(payload.get("endDate"));
        EmployeeAssignment ea = vehicleService.assignToEmployee(id, empId, start, end);
        return ResponseEntity.ok(new EmployeeAssignmentDTO(ea));
    }

}