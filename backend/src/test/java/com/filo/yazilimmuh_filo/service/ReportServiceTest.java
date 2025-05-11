package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.dto.response.ReportDTO;
import com.filo.yazilimmuh_filo.entity.*;
import com.filo.yazilimmuh_filo.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ReportServiceTest {

    @Mock
    private VehicleService vehicleService;
    
    @Mock
    private ExpenseService expenseService;
    
    @Mock
    private AssignmentService assignmentService;
    
    @Mock
    private ReportRepository reportRepository;
    
    @InjectMocks
    private ReportService reportService;
    
    private Vehicle testVehicle;
    private User testUser;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<Expense> expenses;
    private List<Assignment> assignments;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        fromDate = LocalDate.of(2025, 1, 1);
        toDate = LocalDate.of(2025, 3, 31);
        
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setBrand("Toyota");
        testVehicle.setModel("Corolla");
        testVehicle.setPlateNumber("34ABC123");
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPasswordHash("password");
        testUser.setRole(UserRole.ADMIN);
        
        expenses = new ArrayList<>();
        
        Expense fuel1 = new Expense();
        fuel1.setType(ExpenseType.FUEL);
        fuel1.setAmount(100.0);
        fuel1.setDate(LocalDate.of(2025, 1, 15));
        fuel1.setVehicle(testVehicle);
        
        Expense fuel2 = new Expense();
        fuel2.setType(ExpenseType.FUEL);
        fuel2.setAmount(120.0);
        fuel2.setDate(LocalDate.of(2025, 2, 15));
        fuel2.setVehicle(testVehicle);
        
        Expense maintenance = new Expense();
        maintenance.setType(ExpenseType.MAINTENANCE);
        maintenance.setAmount(200.0);
        maintenance.setDate(LocalDate.of(2025, 3, 15));
        maintenance.setVehicle(testVehicle);
        
        Expense outsidePeriod = new Expense();
        outsidePeriod.setType(ExpenseType.FUEL);
        outsidePeriod.setAmount(150.0);
        outsidePeriod.setDate(LocalDate.of(2025, 4, 15));
        outsidePeriod.setVehicle(testVehicle);
        
        expenses.add(fuel1);
        expenses.add(fuel2);
        expenses.add(maintenance);
        expenses.add(outsidePeriod);
        
        assignments = new ArrayList<>();
        
        EmployeeAssignment assignment1 = new EmployeeAssignment();
        assignment1.setStartDate(LocalDate.of(2025, 1, 1));
        assignment1.setEndDate(LocalDate.of(2025, 2, 28));
        assignment1.setVehicle(testVehicle);
        
        PoolAssignment assignment2 = new PoolAssignment();
        assignment2.setStartDate(LocalDate.of(2025, 3, 1));
        assignment2.setEndDate(LocalDate.of(2025, 3, 31));
        assignment2.setVehicle(testVehicle);
        assignment2.setMission("Test Mission");
        
        EmployeeAssignment assignment3 = new EmployeeAssignment();
        assignment3.setStartDate(LocalDate.of(2025, 4, 1));
        assignment3.setEndDate(LocalDate.of(2025, 4, 30));
        assignment3.setVehicle(testVehicle);
        
        assignments.add(assignment1);
        assignments.add(assignment2);
        assignments.add(assignment3);
        
        when(vehicleService.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(vehicleService.getCurrentOdometer(1L)).thenReturn(10000);
        when(vehicleService.getMileage(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(500);
        when(expenseService.findByVehicleId(1L)).thenReturn(expenses);
        when(assignmentService.findByVehicleId(1L)).thenReturn(assignments);
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }
    
    @Test
    public void testGenerateVehicleReport_ValidData() {
        ReportDTO reportDTO = reportService.generateVehicleReport(1L, fromDate, toDate, testUser);
        
        assertNotNull(reportDTO, "Report should not be null");
        assertEquals(10000, reportDTO.currentOdometer(), "Current odometer should match");
        assertEquals(500, reportDTO.mileageInPeriod(), "Mileage should match");
        assertEquals(2, reportDTO.assignmentCount(), "Number of assignments in period should match");
        
        assertTrue(reportDTO.expensesByType().containsKey("FUEL"), "Report should include FUEL expenses");
        assertTrue(reportDTO.expensesByType().containsKey("MAINTENANCE"), "Report should include MAINTENANCE expenses");
        assertEquals(220.0, reportDTO.expensesByType().get("FUEL"), 0.01, "FUEL expenses should sum correctly");
        assertEquals(200.0, reportDTO.expensesByType().get("MAINTENANCE"), 0.01, "MAINTENANCE expenses should sum correctly");
        assertEquals(420.0, reportDTO.totalExpense(), 0.01, "Total expenses should sum correctly");
        
        verify(reportRepository, times(1)).save(any(Report.class));
    }
    
    @Test
    public void testGenerateAllReports() {
        when(vehicleService.findAll()).thenReturn(Arrays.asList(testVehicle));
        
        List<ReportDTO> reports = reportService.generateAllReports(fromDate, toDate, testUser);
        
        assertNotNull(reports, "Reports list should not be null");
        assertEquals(1, reports.size(), "Should generate one report per vehicle");
        
        verify(vehicleService, times(1)).findAll();
    }
} 