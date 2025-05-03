package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Assignment;
import com.filo.yazilimmuh_filo.repository.AssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository repo;

    public AssignmentService(AssignmentRepository repo) {
        this.repo = repo;
    }

    public Optional<Assignment> findById(Long id) {
        return repo.findById(id);
    }
    public List<Assignment> findByVehicleId(Long vehicleId) {
        return repo.findAllByVehicleId(vehicleId);
    }
}