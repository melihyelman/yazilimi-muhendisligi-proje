package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.OdometerReading;
import com.filo.yazilimmuh_filo.repository.OdometerReadingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OdometerReadingService {
    @Autowired
    private OdometerReadingRepository readingRepository;

    public List<OdometerReading> findAll() {
        return readingRepository.findAll();
    }

    public Optional<OdometerReading> findById(Long id) {
        return readingRepository.findById(id);
    }
    public OdometerReading save(OdometerReading reading) {
        return readingRepository.save(reading);
    }

    public void deleteById(Long id) {
        readingRepository.deleteById(id);
    }
    public List<OdometerReading> findByVehicleId(Long vehicleId) {
        return readingRepository.findAllByVehicleId(vehicleId);
    }
}