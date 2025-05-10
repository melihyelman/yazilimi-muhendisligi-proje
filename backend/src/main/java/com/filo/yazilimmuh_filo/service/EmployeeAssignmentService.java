package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.entity.TripLog;
import com.filo.yazilimmuh_filo.repository.EmployeeAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeAssignmentService {
    @Autowired
    private EmployeeAssignmentRepository repo;
    @Autowired
    private TripLogService tripLogService;

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

    public EmployeeAssignment reject(Long id) {
        EmployeeAssignment ea = findById(id).get();
        ea.reject();
        TripLog tripLog = tripLogService.findByAssignment(ea.getId());
        if (tripLog != null) {
            tripLogService.deleteById(tripLog.getId());
        }
        return repo.save(ea);
    }
    public EmployeeAssignment approve(Long id, Long km) {
        EmployeeAssignment ea = findById(id).get();
        ea.approve();
        TripLog tripLog = new TripLog();
        tripLog.setKm(km);
        tripLog.setAssignment(ea);
        tripLogService.create(tripLog);
        return repo.save(ea);
    }
}