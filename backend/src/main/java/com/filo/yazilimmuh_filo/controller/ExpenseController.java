package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.ExpenseDTO;
import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.ExpenseType;
import com.filo.yazilimmuh_filo.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public List<ExpenseDTO> getAll() {
        return expenseService.findAll().stream().map(ExpenseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getById(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(e -> ResponseEntity.ok(new ExpenseDTO(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> create(@RequestBody Expense e,
                                          @RequestParam Long vehicleId,
                                          @RequestParam Long vendorId,
                                          @RequestParam Long validatedBy) {

        Expense saved = expenseService.save(e, vehicleId, vendorId, validatedBy);
        ExpenseDTO dto = new ExpenseDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> update(@PathVariable Long id,
                                          @RequestBody Expense expense) {
        return expenseService.findById(id).map(existing -> {
            if(expense.getAmount() != null) existing.setAmount(expense.getAmount());
            if(expense.getDate() != null) existing.setDate(expense.getDate());
            if(expense.getDescription() != null) existing.setDescription(expense.getDescription());
            if(expense.getVehicle() != null) existing.setVehicle(expense.getVehicle());
            if(expense.getVendor() != null) existing.setVendor(expense.getVendor());
            if(expense.getValidatedBy() != null) existing.setValidatedBy(expense.getValidatedBy());
            if(expense.getType() != null) existing.setType(expense.getType());
            expenseService.create(existing);
            return ResponseEntity.ok(new ExpenseDTO(existing));
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
    @GetMapping("/total")
    public ResponseEntity<Map<String,Double>> totalByType(
            @RequestParam ExpenseType type) {

        double total = expenseService.getTotalByType(type);
        return ResponseEntity.ok(Map.of("total", total));
    }
}