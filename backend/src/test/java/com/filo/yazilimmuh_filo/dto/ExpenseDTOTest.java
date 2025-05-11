package com.filo.yazilimmuh_filo.dto.response;

import com.filo.yazilimmuh_filo.dto.request.VehicleRequestDTO;
import com.filo.yazilimmuh_filo.dto.request.VendorRequestDTO;
import com.filo.yazilimmuh_filo.entity.Expense;
import com.filo.yazilimmuh_filo.entity.ExpenseType;
import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.entity.UserRole;
import com.filo.yazilimmuh_filo.entity.Vendor;
import com.filo.yazilimmuh_filo.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDTOTest {

    private Expense expense;
    private Vehicle vehicle;
    private Vendor vendor;
    private User validator;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        date = LocalDate.of(2025, 5, 11);

        
        vehicle = new Vehicle();
        vehicle.setId(11L);
        vehicle.setPlateNumber("34ABC34");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");

        
        vendor = new Vendor();
        vendor.setId(22L);
        vendor.setUsername("vendorUser");
        vendor.setRole(UserRole.VENDOR);
        vendor.setName("Acme Supplies");
        vendor.setCompanyName("Acme Inc.");

        
        validator = new User();
        validator.setId(33L);
        validator.setUsername("adminUser");
        validator.setRole(UserRole.ADMIN);

        
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
        ExpenseDTO dto = new ExpenseDTO(expense);

        assertEquals(123L, dto.getId(),              "ID doğru set edilmeli");
        assertEquals(date, dto.getDate(),            "Tarih doğru set edilmeli");
        assertEquals("MAINTENANCE", dto.getType(),   "Enum tipi String olarak set edilmeli");
        assertEquals(250.75, dto.getAmount(),        "Tutar doğru set edilmeli");
        assertEquals("Fren balatası değişimi", dto.getDescription(), "Açıklama doğru set edilmeli");

        assertNotNull(dto.getVehicle(), "Vehicle DTO null olmamalı");
        VehicleRequestDTO vDto = dto.getVehicle();
        assertEquals(11L, vDto.getId(), "Vehicle ID doğru map edilmeli");
        assertEquals("34ABC34", vDto.getPlateNumber(), "PlateNumber doğru map edilmeli");
        

        
        assertNotNull(dto.getVendor(), "Vendor DTO null olmamalı");
        VendorRequestDTO vendDto = dto.getVendor();
        assertEquals(22L, vendDto.getId(), "Vendor ID doğru map edilmeli");
        assertEquals("vendorUser", vendDto.getUsername(), "Vendor username doğru map edilmeli");
        assertEquals("Acme Inc.", vendDto.getCompanyName(), "CompanyName doğru map edilmeli");

        
        assertNotNull(dto.getValidatedBy(), "ValidatedBy DTO null olmamalı");
        UserResponse uResp = dto.getValidatedBy();
        assertEquals(33L, uResp.getId(),           "Validator ID doğru map edilmeli");
        assertEquals("adminUser", uResp.getUsername(), "Validator username doğru map edilmeli");
        assertEquals(UserRole.ADMIN, uResp.getRole(),  "Validator role doğru map edilmeli");
    }

    @Test
    void testSettersAndGettersWork() {
        ExpenseDTO dto = new ExpenseDTO();

        
        dto.setId(7L);
        dto.setDate(date.minusDays(5));
        dto.setType("FUEL");
        dto.setAmount(99.9);
        dto.setDescription("Yakıt alımı");

        
        VehicleRequestDTO vDto = new VehicleRequestDTO(vehicle);
        VendorRequestDTO vendDto = new VendorRequestDTO(vendor);
        UserResponse uResp = new UserResponse(validator);

        dto.setVehicle(vDto);
        dto.setVendor(vendDto);
        dto.setValidatedBy(uResp);

        
        assertEquals(7L, dto.getId());
        assertEquals(date.minusDays(5), dto.getDate());
        assertEquals("FUEL", dto.getType());
        assertEquals(99.9, dto.getAmount());
        assertEquals("Yakıt alımı", dto.getDescription());
        assertSame(vDto, dto.getVehicle(),    "Vehicle DTO referansı korunmalı");
        assertSame(vendDto, dto.getVendor(),  "Vendor DTO referansı korunmalı");
        assertSame(uResp, dto.getValidatedBy(),"UserResponse referansı korunmalı");
    }

    @Test
    void testNullsHandledGracefullyInConstructor() {
        Expense empty = new Expense();
        ExpenseDTO dto = new ExpenseDTO(empty);

        assertNull(dto.getId(),          "ID null kalmalı");
        assertNull(dto.getDate(),        "Date null kalmalı");
        assertNull(dto.getType(),        "Type null kalmalı");
        assertNull(dto.getAmount(),      "Amount null kalmalı");
        assertNull(dto.getDescription(), "Description null kalmalı");
        assertNull(dto.getVehicle(),     "Vehicle DTO null kalmalı");
        assertNull(dto.getVendor(),      "Vendor DTO null kalmalı");
        assertNull(dto.getValidatedBy(), "ValidatedBy DTO null kalmalı");
    }
}
