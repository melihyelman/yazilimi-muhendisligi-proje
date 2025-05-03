package com.filo.yazilimmuh_filo.dto;

import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.entity.UserRole;

public class UserResponse {
    private Long id;
    private String username;
    private UserRole role;

    public UserResponse(User u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.role = u.getRole();
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
}