package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Employee;
import com.filo.yazilimmuh_filo.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repo;
    public EmployeeService(EmployeeRepository repo) { this.repo = repo; }
    public List<Employee> findAll() { return repo.findAll(); }
    public Optional<Employee> findById(Long id) { return repo.findById(id); }
    public Employee save(Employee e) { return repo.save(e); }
    public void deleteById(Long id) { repo.deleteById(id); }
}