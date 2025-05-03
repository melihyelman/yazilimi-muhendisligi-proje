package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.Report;
import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.repository.ExpenseRepository;
import com.filo.yazilimmuh_filo.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class ForecastService {
    @Autowired
    private ExpenseRepository expenseRepo;
    @Autowired private ReportRepository reportRepo;
    private int windowSize = 3;

    public ForecastService() {
    }

    public ForecastService(int windowSize) {
        this.windowSize = windowSize;
    }

    @Transactional
    public double generateExpenseForecast(Long vehicleId, User user) {
        List<Expense> list = expenseRepo.findByVehicleId(vehicleId);

        Map<YearMonth, Double> monthlyTotals = list.stream()
                .collect(Collectors.groupingBy(
                        e -> YearMonth.from(e.getDate()),
                        () -> new TreeMap<YearMonth, Double>(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        List<Double> periods = new ArrayList<>(monthlyTotals.values());
        double forecast = calculateMovingAverage(periods);

        Report rpt = new Report();
        rpt.setGeneratedBy(user);
        rpt.setCreatedDate(LocalDate.from(LocalDateTime.now()));
        rpt.setContent("Araç #" + vehicleId + " için tahmin: " + forecast);
        reportRepo.save(rpt);

        return forecast;
    }

    public double calculateMovingAverage(List<Double> data) {
        if (windowSize <= 0 || data.size() < windowSize) {
            throw new IllegalArgumentException("Invalid window size");
        }
        int start = data.size() - windowSize;
        return data.subList(start, data.size()).stream()
                .mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }
}