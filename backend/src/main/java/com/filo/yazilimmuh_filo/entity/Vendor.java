package com.filo.yazilimmuh_filo.entity;

import jakarta.persistence.*;

@Entity
public class Vendor extends User {
    private String name;
    private String companyName;

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
