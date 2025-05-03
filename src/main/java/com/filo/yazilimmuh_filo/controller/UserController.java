package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.UserRequest;
import com.filo.yazilimmuh_filo.dto.UserResponse;
import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponse> listUsers() {
        return userService.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(u -> ResponseEntity.ok(new UserResponse(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest req) {
        User created = userService.create(req.getUsername(), req.getPassword(), req.getRole());
        UserResponse resp = new UserResponse(created);
        return ResponseEntity
                .created(URI.create("/api/users/" + created.getId()))
                .body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest req) {
        return userService.update(id, req.getUsername(), req.getPassword(), req.getRole())
                .map(u -> ResponseEntity.ok(new UserResponse(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}