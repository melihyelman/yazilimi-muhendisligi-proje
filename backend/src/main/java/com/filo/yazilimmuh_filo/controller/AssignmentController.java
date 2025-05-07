package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.AssignmentDTO;
import com.filo.yazilimmuh_filo.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService svc;

    @GetMapping
    public List<AssignmentDTO> getAll() {
        return svc.getAll().stream().map(AssignmentDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getById(@PathVariable Long id) {
        return svc.findById(id)
                .map(a -> ResponseEntity.ok(new AssignmentDTO(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}