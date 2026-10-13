package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
