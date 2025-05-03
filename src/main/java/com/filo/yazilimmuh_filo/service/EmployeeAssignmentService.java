package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.repository.EmployeeAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeAssignmentService {
    private final EmployeeAssignmentRepository repo;

    public EmployeeAssignmentService(EmployeeAssignmentRepository repo) {
        this.repo = repo;
    }

    public List<EmployeeAssignment> findAll() {
        return repo.findAll();
    }

    public Optional<EmployeeAssignment> findById(Long id) {
        return repo.findById(id);
    }

    public EmployeeAssignment save(EmployeeAssignment ea) {
        return repo.save(ea);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public List<EmployeeAssignment> findByEmployeeId(Long id) {
        return (List<EmployeeAssignment>) repo.findByEmployeeId(id);
    }
}