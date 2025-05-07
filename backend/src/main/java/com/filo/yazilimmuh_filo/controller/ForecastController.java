package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.ExpenseType;
import com.filo.yazilimmuh_filo.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/forecast")
public class ForecastController {
    @Autowired
    private ForecastService svc;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
    public ResponseEntity<Map<ExpenseType, Double>> forecast(@RequestParam int period) {
        return ResponseEntity.ok(svc.generateExpenseForecast(period));
    }
}