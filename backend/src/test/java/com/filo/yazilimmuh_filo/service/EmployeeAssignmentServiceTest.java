package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.EmployeeAssignment;
import com.filo.yazilimmuh_filo.entity.TripLog;
import com.filo.yazilimmuh_filo.repository.EmployeeAssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeAssignmentServiceTest {

    @Mock
    private EmployeeAssignmentRepository repo;

    @Mock
    private TripLogService tripLogService;

    @InjectMocks
    private EmployeeAssignmentService employeeAssignmentService;

    private EmployeeAssignment testAssignment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testAssignment = new EmployeeAssignment();
        testAssignment.setId(1L);
    }

    @Test
    public void testReject() {
        TripLog tripLog = new TripLog();
        tripLog.setId(10L);
        when(repo.findById(1L)).thenReturn(Optional.of(testAssignment));
        when(tripLogService.findByAssignment(1L)).thenReturn(tripLog);
        when(repo.save(any(EmployeeAssignment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeAssignment result = employeeAssignmentService.reject(1L);
        verify(tripLogService, times(1)).deleteById(10L);
        assertNotNull(result);
    }

    @Test
    public void testApprove() {
        when(repo.findById(1L)).thenReturn(Optional.of(testAssignment));
        when(repo.save(any(EmployeeAssignment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int km = 100;
        EmployeeAssignment result = employeeAssignmentService.approve(1L, (long) km);
        verify(tripLogService, times(1)).create(any(TripLog.class));
        assertNotNull(result);
    }
}