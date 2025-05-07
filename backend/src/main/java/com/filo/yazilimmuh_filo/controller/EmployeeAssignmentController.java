package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.EmployeeAssignmentDTO;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.repository.EmployeeRepository;
import com.filo.yazilimmuh_filo.repository.VehicleRepository;
import com.filo.yazilimmuh_filo.service.EmployeeAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee-assignments")
public class EmployeeAssignmentController {
    @Autowired
    private EmployeeAssignmentService svc;
    @Autowired
    private VehicleRepository vehicleRepo;
    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping
    public List<EmployeeAssignmentDTO> all() {
        return svc.findAll().stream().map(EmployeeAssignmentDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeAssignmentDTO> one(@PathVariable Long id) {
        return svc.findById(id).map(EmployeeAssignmentDTO::new).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<EmployeeAssignmentDTO> approve(@PathVariable Long id, @RequestParam Long km) {
        return ResponseEntity.ok(new EmployeeAssignmentDTO(svc.approve(id,km)));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<EmployeeAssignmentDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(new EmployeeAssignmentDTO(svc.reject(id)));
    }

    @PostMapping
    public EmployeeAssignmentDTO create(@RequestBody Map<String, Object> payload) {
        EmployeeAssignment ea = new EmployeeAssignment();
        ea.setStartDate(LocalDate.parse((String) payload.get("startDate")));
        ea.setEndDate(LocalDate.parse((String) payload.get("endDate")));

        if (payload.get("vehicle_id") != null) {
            Long vehicleId = Long.valueOf(payload.get("vehicle_id").toString());
            vehicleRepo.findById(vehicleId).ifPresent(ea::setVehicle);
        }

        if (payload.get("employee_id") != null) {
            Long employeeId = Long.valueOf(payload.get("employee_id").toString());
            employeeRepo.findById(employeeId).ifPresent(ea::setEmployee);
        }

        return new EmployeeAssignmentDTO(svc.save(ea));
    }

    @PutMapping("/{id}")
    public EmployeeAssignmentDTO update(@RequestBody Map<String, Object> payload,
                                        @PathVariable Long id) {

        EmployeeAssignment ea = svc.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeAssignment not found"));
        ea.setStartDate(LocalDate.parse((String) payload.get("startDate")));
        ea.setEndDate(LocalDate.parse((String) payload.get("endDate")));

        if (payload.get("vehicle_id") != null) {
            Long vehicleId = Long.valueOf(payload.get("vehicle_id").toString());
            vehicleRepo.findById(vehicleId).ifPresent(ea::setVehicle);
        }

        if (payload.get("employee_id") != null) {
            Long employeeId = Long.valueOf(payload.get("employee_id").toString());
            employeeRepo.findById(employeeId).ifPresent(ea::setEmployee);
        }

        return new EmployeeAssignmentDTO(svc.save(ea));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> del(@PathVariable Long id) {
        if (svc.findById(id).isPresent()) {
            svc.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}