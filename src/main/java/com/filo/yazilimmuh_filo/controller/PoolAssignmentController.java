package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.PoolAssignment;
import com.filo.yazilimmuh_filo.service.PoolAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pool-assignments")
public class PoolAssignmentController {
    private final PoolAssignmentService svc;

    public PoolAssignmentController(PoolAssignmentService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<PoolAssignment> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoolAssignment> one(@PathVariable Long id) {
        return svc.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PoolAssignment create(@RequestBody PoolAssignment pa) {
        return svc.save(pa);
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
