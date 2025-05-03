package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.TripLogDTO;
import com.filo.yazilimmuh_filo.entity.Assignment;
import com.filo.yazilimmuh_filo.entity.TripLog;
import com.filo.yazilimmuh_filo.service.AssignmentService;
import com.filo.yazilimmuh_filo.service.TripLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-logs")
public class TripLogController {
    private final TripLogService svc;
    private final AssignmentService assignmentService;

    public TripLogController(TripLogService logService,
                             AssignmentService assignmentService) {
        this.svc = logService;
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public List<TripLog> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripLog> one(@PathVariable Long id) {
        return svc.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TripLog> create(@RequestBody TripLogDTO dto) {
        Assignment a = assignmentService.findById(dto.assignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
        TripLog log = new TripLog();
        log.setDate(dto.date());
        log.setStartKm(dto.startKm());
        log.setEndKm(dto.endKm());
        log.setAssignment(a);
        TripLog saved = svc.save(log);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (svc.findById(id).isPresent()) {
            svc.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/assignment/{aid}")
    public List<TripLog> byAssignment(@PathVariable("aid") Long assignmentId) {
        return svc.findByAssignment(assignmentId);
    }
}