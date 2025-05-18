package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.*;
import com.filo.yazilimmuh_filo.repository.VehicleRepository;
import com.filo.yazilimmuh_filo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private OdometerReadingService readingService;

    @Mock
    private EmployeeRepository employeeRepo;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle testVehicle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setBrand("Toyota");
        testVehicle.setModel("Corolla");
        testVehicle.setPlateNumber("34ABC123");
    }

    @Test
    public void testFindById() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        Optional<Vehicle> result = vehicleService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Toyota", result.get().getBrand());
    }

    @Test
    public void testUpdate() {
        Vehicle updated = new Vehicle();
        updated.setBrand("Honda");
        updated.setModel("Civic");
        updated.setPlateNumber("06XYZ789");
        updated.setOwnership(OwnershipType.LEASED);
        updated.setLeaseStartDate(LocalDate.now().minusYears(1));
        updated.setLeaseEndDate(LocalDate.now().plusYears(1));

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle result = vehicleService.update(1L, updated);
        assertEquals("Honda", result.getBrand());
        assertEquals("Civic", result.getModel());
        assertEquals("06XYZ789", result.getPlateNumber());
    }

    @Test
    public void testGetCurrentOdometer() {
        OdometerReading reading1 = new OdometerReading();
        reading1.setKm(5000);
        reading1.setDate(LocalDate.now().minusDays(10));

        OdometerReading reading2 = new OdometerReading();
        reading2.setKm(5500);
        reading2.setDate(LocalDate.now().minusDays(1));

        when(readingService.findByVehicleId(1L)).thenReturn(Arrays.asList(reading1, reading2));
        int currentKm = vehicleService.getCurrentOdometer(1L);
        assertEquals(5500, currentKm);
    }

    @Test
    public void testAssignToPool() {
        testVehicle.getAssignments().clear();
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String mission = "Delivery Mission";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(5);
        PoolAssignment poolAssignment = vehicleService.assignToPool(1L, mission, start, end);

        assertNotNull(poolAssignment);
        assertEquals(mission, poolAssignment.getMission());
        assertEquals(start, poolAssignment.getStartDate());
        assertEquals(end, poolAssignment.getEndDate());
        assertEquals(testVehicle, poolAssignment.getVehicle());
    }

    @Test
    public void testAssignToEmployee() {
        Employee testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setName("John Doe");
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);
        EmployeeAssignment assignment = vehicleService.assignToEmployee(1L, 1L, start, end);

        assertNotNull(assignment);
        assertEquals(testVehicle, assignment.getVehicle());
        assertEquals(testEmployee, assignment.getEmployee());
        assertEquals(start, assignment.getStartDate());
        assertEquals(end, assignment.getEndDate());
    }
}