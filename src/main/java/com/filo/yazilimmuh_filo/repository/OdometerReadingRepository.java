package com.filo.yazilimmuh_filo.repository;

import com.filo.yazilimmuh_filo.entity.OdometerReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OdometerReadingRepository extends JpaRepository<OdometerReading, Long> {
    List<OdometerReading> findAllByVehicleId(Long vehicleId);
}
