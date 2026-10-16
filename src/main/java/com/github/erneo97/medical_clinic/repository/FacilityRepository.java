package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Facility;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    @EntityGraph(attributePaths = {"doctors"})
    @Query("SELECT f FROM Facility f")
    List<Facility> findAllWithDoctors();

}
