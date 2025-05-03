package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.TripLog;
import com.filo.yazilimmuh_filo.repository.TripLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripLogService {
    private final TripLogRepository repo;

    public TripLogService(TripLogRepository repo) {
        this.repo = repo;
    }

    public List<TripLog> findAll() {
        return repo.findAll();
    }

    public Optional<TripLog> findById(Long id) {
        return repo.findById(id);
    }

    public TripLog save(TripLog log) {
        return repo.save(log);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public List<TripLog> findByAssignment(Long assignmentId) {
        return repo.findAllByAssignmentId(assignmentId);
    }
}