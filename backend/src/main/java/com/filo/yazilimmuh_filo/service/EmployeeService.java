package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Employee;
import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.entity.Vehicle;
import com.filo.yazilimmuh_filo.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepository repo;
    @Autowired
    private VehicleService vehicleRepo;
    public List<Employee> findAll() {
        return repo.findAll();
    }

    public Optional<Employee> findById(Long id) {
        return repo.findById(id);
    }

    public Employee save(Employee e) {
        return repo.save(e);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public EmployeeAssignment assignVehicle(Long empId,
                                            Long vehicleId,
                                            LocalDate start,
                                            LocalDate end) {
        Employee e = findById(empId).get();
        Vehicle v = vehicleRepo.findById(vehicleId).get();
        EmployeeAssignment ea = e.assignVehicle(v, start, end);
        repo.save(e);
        return ea;
    }
}