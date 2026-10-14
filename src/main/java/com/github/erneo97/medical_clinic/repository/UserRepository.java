package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Role;
import com.github.erneo97.medical_clinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByIdAndRole(Long id, Role role);
}
