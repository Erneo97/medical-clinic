package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    boolean existsByEmail(String email);

    void deleteById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Patient p SET p.password = :password WHERE p.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String password);
}
