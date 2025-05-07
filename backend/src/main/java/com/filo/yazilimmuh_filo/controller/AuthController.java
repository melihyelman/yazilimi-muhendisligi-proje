package com.filo.yazilimmuh_filo.controller;

import com.filo.yazilimmuh_filo.dto.response.JwtResponse;
import com.filo.yazilimmuh_filo.dto.request.LoginRequest;
import com.filo.yazilimmuh_filo.dto.request.UserRequest;
import com.filo.yazilimmuh_filo.dto.response.UserResponse;
import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.security.JwtTokenProvider;
import com.filo.yazilimmuh_filo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        String jwt = tokenProvider.generateToken(auth);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest req) {
        User u = userService.create(req.getUsername(), req.getPassword(), req.getRole());
        return ResponseEntity
                .created(URI.create("/api/users/" + u.getId()))
                .body(new UserResponse(u));
    }
}