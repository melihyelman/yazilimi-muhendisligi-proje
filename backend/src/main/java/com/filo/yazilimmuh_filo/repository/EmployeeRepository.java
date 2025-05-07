package com.filo.yazilimmuh_filo.repository;

import com.filo.yazilimmuh_filo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
