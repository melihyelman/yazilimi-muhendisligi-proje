package com.filo.yazilimmuh_filo.dto.request;

import com.filo.yazilimmuh_filo.entity.UserRole;
import com.filo.yazilimmuh_filo.entity.Vendor;

public class VendorRequestDTO {

    private Long id;
    private String username;
    private UserRole role;
    private String name;
    private String companyName;

    public VendorRequestDTO() {
    }

    public VendorRequestDTO(Long id, String username, UserRole role, String name, String companyName) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.name = name;
        this.companyName = companyName;
    }

    public VendorRequestDTO(Vendor vendor) {
        this.id = vendor.getId();
        this.username = vendor.getUsername();
        this.role = vendor.getRole();
        this.name = vendor.getName();
        this.companyName = vendor.getCompanyName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}