package com.filo.yazilimmuh_filo.repository;

import com.filo.yazilimmuh_filo.entity.TripLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripLogRepository extends JpaRepository<TripLog, Long> {
    List<TripLog> findAllByAssignmentId(Long assignmentId);
}
