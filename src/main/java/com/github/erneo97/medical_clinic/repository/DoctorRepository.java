package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Doctor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @EntityGraph(attributePaths = {"user", "facilities"})
    @Query("select d from Doctor d")
    List<Doctor> findAllWithUserAndFacilities();
}
