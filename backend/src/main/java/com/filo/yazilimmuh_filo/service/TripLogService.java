package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Assignment;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.entity.TripLog;
import com.filo.yazilimmuh_filo.repository.TripLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripLogService {
    @Autowired
    private TripLogRepository repo;

    public List<TripLog> findAll() {
        return repo.findAll();
    }

    public Optional<TripLog> findById(Long id) {
        return repo.findById(id);
    }

    public TripLog create(TripLog log) {
        return repo.save(log);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public TripLog findByAssignment(Long assignmentId) {
        return repo.findByAssignmentId(assignmentId);
    }
}