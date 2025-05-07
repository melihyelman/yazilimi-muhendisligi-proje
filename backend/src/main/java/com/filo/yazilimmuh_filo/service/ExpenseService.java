package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.ExpenseType;
import com.filo.yazilimmuh_filo.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private VehicleService vehicleRepo;
    @Autowired
    private VendorService vendorRepo;
    @Autowired
    private UserService userRepo;

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    public Expense save(Expense e, Long vehicleId, Long vendorId, Long validatedById) {
        vehicleRepo.findById(vehicleId).ifPresent(e::setVehicle);
        vendorRepo.findById(vendorId).ifPresent(e::setVendor);
        userRepo.findById(validatedById).ifPresent(e::setValidatedBy);
        return expenseRepository.save(e);
    }
    public Expense create(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> findByVehicleId(Long vehicleId) {
        return expenseRepository.findAllByVehicleId(vehicleId);
    }

    public double getTotalByType(ExpenseType type) {
        List<Expense> all = expenseRepository.findAll();
        return Expense.getTotalByType(all, type);
    }
}