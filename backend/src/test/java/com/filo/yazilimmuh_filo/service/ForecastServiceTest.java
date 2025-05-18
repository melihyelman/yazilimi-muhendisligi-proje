package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.ExpenseType;
import com.filo.yazilimmuh_filo.entity.Vehicle;
import com.filo.yazilimmuh_filo.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ForecastServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ForecastService forecastService;

    private List<Expense> testExpenses;
    private Vehicle testVehicle;
    private LocalDate now;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        now = LocalDate.now();
        testExpenses = new ArrayList<>();
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setBrand("Toyota");
        testVehicle.setModel("Corolla");
        
        createExpense(ExpenseType.FUEL, 100.0, now.minusMonths(2));
        createExpense(ExpenseType.FUEL, 120.0, now.minusMonths(1));
        createExpense(ExpenseType.FUEL, 110.0, now.minusMonths(0));
        
        createExpense(ExpenseType.MAINTENANCE, 200.0, now.minusMonths(2));
        createExpense(ExpenseType.MAINTENANCE, 0.0, now.minusMonths(1));
        createExpense(ExpenseType.MAINTENANCE, 400.0, now.minusMonths(0));
        
        createExpense(ExpenseType.INSURANCE, 600.0, now.minusMonths(0));
        
        when(expenseRepository.findAll()).thenReturn(testExpenses);
    }
    
    private void createExpense(ExpenseType type, double amount, LocalDate date) {
        Expense expense = new Expense();
        expense.setType(type);
        expense.setAmount(amount);
        expense.setDate(date);
        expense.setVehicle(testVehicle);
        testExpenses.add(expense);
    }
    
    @Test
    public void testGenerateExpenseForecast_ThreeMonthPeriod() {
        Map<ExpenseType, Double> forecast = forecastService.generateExpenseForecast(3);
        
        assertTrue(forecast.containsKey(ExpenseType.FUEL), "Forecast should contain FUEL expenses");
        assertTrue(forecast.containsKey(ExpenseType.MAINTENANCE), "Forecast should contain MAINTENANCE expenses");
        assertTrue(forecast.containsKey(ExpenseType.INSURANCE), "Forecast should contain INSURANCE expenses");
        
        verify(expenseRepository, times(1)).findAll();
        
        final double DELTA = 0;
        assertEquals(110, forecast.get(ExpenseType.FUEL), DELTA, "FUEL expense forecast should match expected value");
        assertEquals(200, forecast.get(ExpenseType.MAINTENANCE), DELTA, "MAINTENANCE expense forecast should match expected value");
        assertEquals(200, forecast.get(ExpenseType.INSURANCE), DELTA, "INSURANCE expense forecast should match expected value");
    }
    
    @Test
    public void testGenerateExpenseForecast_OneMonthPeriod() {
        Map<ExpenseType, Double> forecast = forecastService.generateExpenseForecast(1);
        
        assertTrue(forecast.containsKey(ExpenseType.FUEL), "Forecast should contain FUEL expenses");
        assertTrue(forecast.containsKey(ExpenseType.MAINTENANCE), "Forecast should contain MAINTENANCE expenses");
        assertTrue(forecast.containsKey(ExpenseType.INSURANCE), "Forecast should contain INSURANCE expenses");
        
        verify(expenseRepository, times(1)).findAll();
        
        final double DELTA = 0;
        assertEquals(110, forecast.get(ExpenseType.FUEL), DELTA, "FUEL expense forecast should match expected value");
        assertEquals(400, forecast.get(ExpenseType.MAINTENANCE), DELTA, "MAINTENANCE expense forecast should match expected value");
        assertEquals(600, forecast.get(ExpenseType.INSURANCE), DELTA, "INSURANCE expense forecast should match expected value");
    }
    
    @Test
    public void testGenerateExpenseForecast_NoExpenses() {
        when(expenseRepository.findAll()).thenReturn(new ArrayList<>());
        
        Map<ExpenseType, Double> forecast = forecastService.generateExpenseForecast(3);
        
        assertTrue(forecast.isEmpty(), "Forecast should be empty when there are no expenses");
    }
} 