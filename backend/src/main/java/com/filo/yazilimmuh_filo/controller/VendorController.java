package com.filo.yazilimmuh_filo.controller;
import com.filo.yazilimmuh_filo.dto.response.VendorDTO;
import com.filo.yazilimmuh_filo.entity.Vendor;
import com.filo.yazilimmuh_filo.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {
    @Autowired
    private VendorService svc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<VendorDTO> create(@RequestBody Vendor v) {

        v.setPasswordHash(passwordEncoder.encode(v.getPasswordHash()));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new VendorDTO(svc.save(v))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(new VendorDTO(svc.findById(id).get()));
    }

    @GetMapping
    public ResponseEntity<List<VendorDTO>> list() {
        return ResponseEntity.ok(svc.findAll().stream().map(VendorDTO::new).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDTO> update(@PathVariable Long id,
                                         @RequestBody Vendor v) {
        v.setId(id);
        if (v.getPasswordHash() != null) {
            v.setPasswordHash(passwordEncoder.encode(v.getPasswordHash()));
        }
        return ResponseEntity.ok(
                new VendorDTO(svc.update(id, v))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}