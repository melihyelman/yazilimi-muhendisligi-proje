package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forecast")
public class ForecastController {
    @Autowired
    private ForecastService svc;

    @GetMapping("/{vehicleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> forecast(
            @PathVariable Long vehicleId,
            @AuthenticationPrincipal User user
    ) {
        double f = svc.generateExpenseForecast(vehicleId, user);
        return ResponseEntity.ok(f);
    }
}