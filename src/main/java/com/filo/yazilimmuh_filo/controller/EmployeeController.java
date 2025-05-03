package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.Employee;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.service.EmployeeAssignmentService;
import com.filo.yazilimmuh_filo.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService svc;
    private final EmployeeAssignmentService eaService;


    public EmployeeController(EmployeeService svc,
                              EmployeeAssignmentService eaService) {
        this.svc = svc;
        this.eaService = eaService;
    }

    @GetMapping
    public List<Employee> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> one(@PathVariable Long id) {
        return svc.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Employee create(@RequestBody Employee e) {
        return svc.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee e) {
        return svc.findById(id)
                .map(ex -> {
                            ex.setName(e.getName());
                            ex.setDepartment(e.getDepartment());
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

    @GetMapping("/{id}/assignments")
    public List<EmployeeAssignment> getAssignments(@PathVariable Long id) {
        return eaService.findByEmployeeId(id);
    }
}