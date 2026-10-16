package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
