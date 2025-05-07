package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.PoolAssignmentDTO;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.entity.PoolAssignment;
import com.filo.yazilimmuh_filo.repository.PoolAssignmentRepository;
import com.filo.yazilimmuh_filo.repository.VehicleRepository;
import com.filo.yazilimmuh_filo.service.PoolAssignmentService;
import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pool-assignments")
public class PoolAssignmentController {
    @Autowired
    private PoolAssignmentService svc;
    @Autowired
    private VehicleRepository vehicleRepo;

    @GetMapping
    public List<PoolAssignmentDTO> all() {
        return svc.findAll().stream()
                .map(PoolAssignmentDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoolAssignmentDTO> one(@PathVariable Long id) {
        return svc.findById(id).map(p -> ResponseEntity.ok(new PoolAssignmentDTO(p))).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PoolAssignmentDTO> updateMission(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        PoolAssignment p = svc.findById(id).orElseThrow(() -> new RuntimeException("PoolAssignment not found"));
        if (payload.get("startDate") != null) {
            p.setStartDate(LocalDate.parse(payload.get("startDate")));
        }
        if (payload.get("endDate") != null) {
            p.setEndDate(LocalDate.parse(payload.get("endDate")));
        }

        if (payload.get("vehicle_id") != null) {
            Long vehicleId = Long.valueOf(payload.get("vehicle_id").toString());
            vehicleRepo.findById(vehicleId).ifPresent(p::setVehicle);
        }
        if (payload.get("mission") != null) {
            p.setMission(payload.get("mission"));
        }
        return ResponseEntity.ok(new PoolAssignmentDTO(svc.save(p)));
    }

    @PostMapping
    public PoolAssignmentDTO create(@RequestBody Map<String, Object> payload) {
        PoolAssignment pa = new PoolAssignment();
        pa.setStartDate(LocalDate.parse((String) payload.get("startDate")));
        pa.setEndDate(LocalDate.parse((String) payload.get("endDate")));
        pa.setMission((String) payload.get("mission"));

        if (payload.get("vehicle_id") != null) {
            Long vehicleId = Long.valueOf(payload.get("vehicle_id").toString());
            vehicleRepo.findById(vehicleId).ifPresent(pa::setVehicle);
        }

        return new PoolAssignmentDTO(svc.save(pa));
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
