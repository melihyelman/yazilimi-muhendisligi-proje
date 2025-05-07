package com.filo.yazilimmuh_filo.service;


import com.filo.yazilimmuh_filo.entity.UserRole;
import com.filo.yazilimmuh_filo.entity.Vendor;
import com.filo.yazilimmuh_filo.repository.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    public Vendor save(Vendor vendor) {
        vendor.setRole(UserRole.VENDOR);
        return vendorRepository.save(vendor);
    }

    public List<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    public Optional<Vendor> findById(Long id) {
        return vendorRepository.findById(id);
    }

    public void deleteById(Long id) {
        vendorRepository.deleteById(id);
    }

    public Vendor update(Long id, Vendor newData) {
        Vendor existing = vendorRepository.findById(id).get();
        existing.setName(newData.getName());
        existing.setId(newData.getId());
        return vendorRepository.save(existing);
    }
}
