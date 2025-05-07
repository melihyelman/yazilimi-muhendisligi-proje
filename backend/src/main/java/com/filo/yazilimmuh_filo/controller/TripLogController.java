package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.TripLogDTO;
import com.filo.yazilimmuh_filo.entity.TripLog;
import com.filo.yazilimmuh_filo.service.AssignmentService;
import com.filo.yazilimmuh_filo.service.TripLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trip-logs")
public class TripLogController {
    @Autowired
    private TripLogService svc;

    @GetMapping
    public List<TripLogDTO> getAll() {
        return svc.findAll().stream().map(TripLogDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripLogDTO> getById(@PathVariable Long id) {
        return svc.findById(id)
                .map(t -> ResponseEntity.ok(new TripLogDTO(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/assignment/{aid}")
    public TripLogDTO byAssignment(@PathVariable("aid") Long assignmentId) {
        return new TripLogDTO(svc.findByAssignment(assignmentId));
    }
}