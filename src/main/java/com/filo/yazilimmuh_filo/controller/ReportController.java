package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.ReportDTO;
import com.filo.yazilimmuh_filo.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    @GetMapping("/vehicle/{id}")
    public ReportDTO vehicleReport(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return reportService.generateVehicleReport(id, from, to);
    }
}
