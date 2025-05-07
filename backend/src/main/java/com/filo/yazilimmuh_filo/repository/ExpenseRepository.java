package com.filo.yazilimmuh_filo.repository;

import com.filo.yazilimmuh_filo.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByVehicleId(Long vehicleId);

    List<Expense> findByVehicleId(Long vehicleId);
}
