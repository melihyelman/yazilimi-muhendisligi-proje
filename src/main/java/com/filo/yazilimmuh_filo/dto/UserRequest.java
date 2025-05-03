package com.filo.yazilimmuh_filo.dto;

import com.filo.yazilimmuh_filo.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private UserRole role;

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public @NotNull UserRole getRole() {
        return role;
    }

    public void setRole(@NotNull UserRole role) {
        this.role = role;
    }
}