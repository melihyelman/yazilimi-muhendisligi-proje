package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.dto.ReportDTO;
import com.filo.yazilimmuh_filo.entity.Expense;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final VehicleService vehicleService;
    private final OdometerReadingService readingService;
    private final ExpenseService expenseService;
    private final ForecastService forecastService;

    public ReportService(VehicleService vehicleService,
                         OdometerReadingService readingService,
                         ExpenseService expenseService,
                         ForecastService forecastService) {
        this.vehicleService = vehicleService;
        this.readingService = readingService;
        this.expenseService = expenseService;
        this.forecastService = forecastService;
    }

    /**
     * Verilen araç için özet rapor oluşturur.
     * @param vehicleId Araç ID
     * @param from Başlangıç tarihi (dahil)
     * @param to Bitiş tarihi (dahil)
     */
    public ReportDTO generateVehicleReport(Long vehicleId, LocalDate from, LocalDate to) {
        // Mevcut sayaç
        int currentOdo = vehicleService.getCurrentOdometer(vehicleId);
        // Dönem içi kilometre
        int mileage = vehicleService.getMileage(vehicleId, from, to);

        // Masrafları al
        List<Expense> expenses = expenseService.findByVehicleId(vehicleId);
        // İstenilen periyottaki masraflar
        List<Expense> periodExpenses = expenses.stream()
                .filter(e -> !e.getDate().isBefore(from) && !e.getDate().isAfter(to))
                .toList();
        // Tür bazında toplam
        Map<String, Double> byType = periodExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getType().name(),
                        Collectors.summingDouble(Expense::getAmount)
                ));
        double totalExp = periodExpenses.stream()
                .mapToDouble(Expense::getAmount).sum();

        // Hareketli ortalama tahmin
        // windowSize parametresi service içinde ayarlanabilir
        List<Double> amounts = expenses.stream()
                .map(Expense::getAmount).toList();
        double forecast = forecastService.calculateMovingAverage(amounts);

        return new ReportDTO(
                vehicleId,
                currentOdo,
                mileage,
                byType,
                totalExp,
                forecast
        );
    }
}