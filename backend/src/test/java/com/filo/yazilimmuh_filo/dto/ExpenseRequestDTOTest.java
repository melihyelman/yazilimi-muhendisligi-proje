package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.ExpenseType;
import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.entity.Vendor;
import com.filo.yazilimmuh_filo.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseRequestDTOTest {

    private Expense expense;
    private Vehicle vehicle;
    private Vendor vendor;
    private User validator;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        
        date = LocalDate.of(2025, 5, 11);

        vehicle = new Vehicle();
        vehicle.setId(42L);

        vendor = new Vendor();
        vendor.setId(17L);

        validator = new User();
        validator.setId(99L);

        expense = new Expense();
        expense.setId(123L);
        expense.setDate(date);
        expense.setType(ExpenseType.MAINTENANCE);
        expense.setAmount(250.75);
        expense.setDescription("Fren balatası değişimi");

        expense.setVehicle(vehicle);
        expense.setVendor(vendor);
        expense.setValidatedBy(validator);
    }

    @Test
    void testConstructorMapsAllFieldsCorrectly() {
        ExpenseRequestDTO dto = new ExpenseRequestDTO(expense);

        assertEquals(123L, dto.getId(),              "ID doğru kopyalanmalı");
        assertEquals(date, dto.getDate(),            "Tarih doğru kopyalanmalı");
        assertEquals("MAINTENANCE", dto.getType(),   "Tür enum’dan String’e dönüştürülmeli");
        assertEquals(250.75, dto.getAmount(),        "Tutar doğru kopyalanmalı");
        assertEquals("Fren balatası değişimi", dto.getDescription(), "Açıklama doğru kopyalanmalı");
        assertEquals(42L, dto.getVehicleId(),       "Vehicle ID doğru kopyalanmalı");
        assertEquals(17L, dto.getVendorId(),        "Vendor ID doğru kopyalanmalı");
        assertEquals(99L, dto.getValidatedById(),   "ValidatedBy ID doğru kopyalanmalı");
    }

    @Test
    void testSettersAndGettersWork() {
        ExpenseRequestDTO dto = new ExpenseRequestDTO();

        dto.setId(7L);
        dto.setDate(date.minusDays(5));
        dto.setType("FUEL");
        dto.setAmount(99.9);
        dto.setDescription("Yakıt alımı");
        dto.setVehicleId(5L);
        dto.setVendorId(6L);
        dto.setValidatedById(8L);

        assertEquals(7L, dto.getId());
        assertEquals(date.minusDays(5), dto.getDate());
        assertEquals("FUEL", dto.getType());
        assertEquals(99.9, dto.getAmount());
        assertEquals("Yakıt alımı", dto.getDescription());
        assertEquals(5L, dto.getVehicleId());
        assertEquals(6L, dto.getVendorId());
        assertEquals(8L, dto.getValidatedById());
    }

    @Test
    void testNullsHandledGracefullyInConstructor() {
        
        Expense e2 = new Expense();
        ExpenseRequestDTO dto = new ExpenseRequestDTO(e2);

        assertNull(dto.getId());
        assertNull(dto.getDate());
        assertNull(dto.getType());
        assertNull(dto.getAmount());
        assertNull(dto.getDescription());
        assertNull(dto.getVehicleId());
        assertNull(dto.getVendorId());
        assertNull(dto.getValidatedById());
    }
}
