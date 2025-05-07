package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Assignment;
import com.filo.yazilimmuh_filo.repository.AssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {
    @Autowired
    private AssignmentRepository repo;

    public List<Assignment> getAll() {
        return repo.findAll();
    }
    public Optional<Assignment> findById(Long id) {
        return repo.findById(id);
    }
    public List<Assignment> findByVehicleId(Long vehicleId) {
        return repo.findAllByVehicleId(vehicleId);
    }
    public void delete(Long id) {
        repo.delete(findById(id).get());
    }
}