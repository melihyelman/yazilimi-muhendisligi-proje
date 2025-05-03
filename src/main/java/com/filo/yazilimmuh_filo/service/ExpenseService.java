package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> findByVehicleId(Long vehicleId) {
        return expenseRepository.findAllByVehicleId(vehicleId);
    }
}