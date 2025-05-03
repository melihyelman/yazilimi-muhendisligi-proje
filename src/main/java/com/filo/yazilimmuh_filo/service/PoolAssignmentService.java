package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.PoolAssignment;
import com.filo.yazilimmuh_filo.repository.PoolAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PoolAssignmentService {
    private final PoolAssignmentRepository repo;
    public PoolAssignmentService(PoolAssignmentRepository repo) { this.repo = repo; }
    public List<PoolAssignment> findAll() { return repo.findAll(); }
    public Optional<PoolAssignment> findById(Long id) { return repo.findById(id); }
    public PoolAssignment save(PoolAssignment pa) { return repo.save(pa); }
    public void deleteById(Long id) { repo.deleteById(id); }
}