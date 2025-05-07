package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.EmployeeAssignmentDTO;
import com.filo.yazilimmuh_filo.entity.Employee;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.dto.response.EmployeeDTO;
import com.filo.yazilimmuh_filo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService svc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<EmployeeDTO> all() {
        return svc.findAll().stream().map(EmployeeDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> one(@PathVariable Long id) {
        return svc.findById(id).map(e -> ResponseEntity.ok(new EmployeeDTO(e))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Employee create(@RequestBody Employee e) {
        e.setPasswordHash(passwordEncoder.encode(e.getPasswordHash()));
        return svc.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee e) {
        return svc.findById(id)
                .map(ex -> {
                            if (e.getName() != null) ex.setName(e.getName());
                            if (e.getDepartment() != null) ex.setDepartment(e.getDepartment());
                            if (e.getAssignments() != null) ex.setAssignments(e.getAssignments());
                            if (e.getUsername() != null) ex.setUsername(e.getUsername());
                            if (e.getRole() != null) ex.setRole(e.getRole());
                            svc.save(ex);
                            return ResponseEntity.ok(ex);
                        }
                ).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> del(@PathVariable Long id) {
        if (svc.findById(id).isPresent()) {
            svc.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/assignVehicle")
    public ResponseEntity<EmployeeAssignmentDTO> assignVehicle(
            @PathVariable Long id,
            @RequestBody Map<String,String> payload) {

        Long vehicleId = Long.valueOf(payload.get("vehicleId"));
        LocalDate start = LocalDate.parse(payload.get("startDate"));
        LocalDate end   = LocalDate.parse(payload.get("endDate"));
        EmployeeAssignment ea = svc.assignVehicle(id, vehicleId, start, end);
        return ResponseEntity.ok(new EmployeeAssignmentDTO(ea));
    }
}