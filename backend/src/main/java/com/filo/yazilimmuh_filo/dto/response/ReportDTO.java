package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.VehicleRequestDTO;

import java.util.Map;

public record ReportDTO(
        VehicleRequestDTO vehicle,
        int currentOdometer,
        int mileageInPeriod,
        Map<String, Double> expensesByType,
        double totalExpense,
        int assignmentCount
) {}
