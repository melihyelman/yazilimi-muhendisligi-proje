package com.filo.yazilimmuh_filo.dto;

import java.util.Map;

public record ReportDTO(
        Long vehicleId,
        int currentOdometer,
        int mileageInPeriod,
        Map<String, Double> expensesByType,
        double totalExpense,
        double forecastExpense
) {}