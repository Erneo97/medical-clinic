package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserEmail(String email);

    void deleteById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
        UPDATE User u
        SET u.password = :password
        WHERE u.id = (SELECT p.user.id FROM Patient p WHERE p.id = :id)
    """)
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    @Query("""
        SELECT p
        FROM Patient p
        WHERE lower(p.lastName) like lower(concat('%', :fragment, '%'))
            or lower(p.firstName) like lower(concat('%', :fragment, '%'))
    """)
    List<Patient> searchByName(@Param("fragment") String fragment);
}
