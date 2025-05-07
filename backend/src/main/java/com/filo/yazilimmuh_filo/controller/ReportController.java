package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.ReportDTO;
import com.filo.yazilimmuh_filo.entity.Report;
import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.repository.UserRepository;
import com.filo.yazilimmuh_filo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")

    public List<ReportDTO> generateReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @AuthenticationPrincipal UserDetails userDetails

    ) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        return reportService.generateAllReports(from, to, user.get());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Report> getAllReports() {
        return reportService.findAll();
    }


    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ReportDTO generateCehicleReport(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @AuthenticationPrincipal UserDetails userDetails

    ) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        return reportService.generateVehicleReport(id, from, to, user.get());
    }
}
