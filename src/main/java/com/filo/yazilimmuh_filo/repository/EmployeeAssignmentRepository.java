package com.filo.yazilimmuh_filo.repository;

import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeAssignmentRepository extends JpaRepository<EmployeeAssignment, Long> {
    public List<EmployeeAssignment> findByEmployeeId(Long id);
}
