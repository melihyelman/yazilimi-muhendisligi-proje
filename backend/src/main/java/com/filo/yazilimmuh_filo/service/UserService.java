package com.filo.yazilimmuh_filo.service;

import com.filo.yazilimmuh_filo.entity.User;
import com.filo.yazilimmuh_filo.entity.UserRole;
import com.filo.yazilimmuh_filo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String loginField)
            throws UsernameNotFoundException {

        User user = userRepo.findByUsername(loginField)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + loginField));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .build();
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public User create(String username, String rawPassword, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return userRepo.save(user);
    }

    public Optional<User> update(Long id,
                                 String username,
                                 String rawPassword,
                                 UserRole role) {
        return userRepo.findById(id)
                .map(user -> {
                    user.setUsername(username);
                    if (rawPassword != null && !rawPassword.isBlank()) {
                        user.setPasswordHash(passwordEncoder.encode(rawPassword));
                    }
                    user.setRole(role);
                    return userRepo.save(user);
                });
    }

    public boolean delete(Long id) {
        if (!userRepo.existsById(id)) {
            return false;
        }
        userRepo.deleteById(id);
        return true;
    }
}
