package com.libraryapp.repository;

import com.libraryapp.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *  Repository used to access AppUser data from the database
 */

public interface AppUserRepository extends JpaRepository <AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
