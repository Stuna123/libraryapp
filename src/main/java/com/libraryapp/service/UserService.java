package com.libraryapp.service;

import com.libraryapp.entity.AppUser;
import com.libraryapp.entity.Role;
import com.libraryapp.form.RegisterForm;
import com.libraryapp.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user-related business operations.
 *
 * It handles user registration, password hashing and default role assignment.
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterForm form) {
        if (appUserRepository.existsByEmail(form.getEmail())) {
           throw new RuntimeException("Un compte existe déjà avec cet email.");
        }

        AppUser appUser = AppUser.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .role(Role.USER)
                .build();

        appUserRepository.save(appUser);
    }

}
