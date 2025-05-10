package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.ExpenseType;
import com.filo.yazilimmuh_filo.repository.ExpenseRepository;
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

    public ForecastService() {
    }

    @Transactional
    public Map<ExpenseType, Double> generateExpenseForecast(int periodInMonths) {
        LocalDate now = LocalDate.now();
        LocalDate fromDate = now.minusMonths(periodInMonths);

        List<Expense> filteredExpenses = expenseRepo.findAll().stream()
                .filter(e -> e.getDate() != null && !e.getDate().isBefore(fromDate) && !e.getDate().isAfter(now))
                .collect(Collectors.toList());

        Map<ExpenseType, List<Expense>> expensesByType = filteredExpenses.stream()
                .collect(Collectors.groupingBy(Expense::getType));

        Map<ExpenseType, Double> forecastByType = new TreeMap<>();

        for (ExpenseType type : expensesByType.keySet()) {
            List<Expense> expenses = expensesByType.get(type);

            Map<YearMonth, Double> monthlyTotals = expenses.stream()
                    .collect(Collectors.groupingBy(
                            e -> YearMonth.from(e.getDate()),
                            TreeMap::new,
                            Collectors.summingDouble(Expense::getAmount)
                    ));

            monthlyTotals = fillMissingMonths(monthlyTotals, fromDate, now);

            List<Double> monthlySums = new ArrayList<>(monthlyTotals.values());
            int startIndex = Math.max(0, monthlySums.size() - periodInMonths);
            List<Double> lastNMonths = monthlySums.subList(startIndex, monthlySums.size());

            double average = calculateSimpleAverage(lastNMonths);

            forecastByType.put(type, average);
        }

        return forecastByType;
    }


    private Map<YearMonth, Double> fillMissingMonths(Map<YearMonth, Double> original, LocalDate from, LocalDate to) {
        Map<YearMonth, Double> completeMap = new TreeMap<>();
        YearMonth startMonth = YearMonth.from(from);
        YearMonth endMonth = YearMonth.from(to);

        YearMonth current = startMonth;
        while (!current.isAfter(endMonth)) {
            completeMap.put(current, original.getOrDefault(current, 0.0));
            current = current.plusMonths(1);
        }

        return completeMap;
    }

    private double calculateSimpleAverage(List<Double> data) {
        if (data.isEmpty()) return 0.0;
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}