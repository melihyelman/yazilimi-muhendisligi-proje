package com.filo.yazilimmuh_filo.repository;

import com.filo.yazilimmuh_filo.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByVehicleId(Long vehicleId);
}
