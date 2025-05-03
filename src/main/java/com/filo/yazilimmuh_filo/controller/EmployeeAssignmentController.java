package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.service.EmployeeAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-assignments")
public class EmployeeAssignmentController {
    private final EmployeeAssignmentService svc;

    public EmployeeAssignmentController(EmployeeAssignmentService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<EmployeeAssignment> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeAssignment> one(@PathVariable Long id) {
        return svc.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EmployeeAssignment create(@RequestBody EmployeeAssignment ea) {
        return svc.save(ea);
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