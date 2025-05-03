package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getAll() {
        return expenseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Expense create(@RequestBody Expense expense) {
        return expenseService.save(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable Long id,
                                          @RequestBody Expense expense) {
        return expenseService.findById(id).map(existing -> {
            existing.setDate(expense.getDate());
            existing.setType(expense.getType());
            existing.setAmount(expense.getAmount());
            existing.setDescription(expense.getDescription());
            existing.setVehicle(expense.getVehicle());
            expenseService.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (expenseService.findById(id).isPresent()) {
            expenseService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}