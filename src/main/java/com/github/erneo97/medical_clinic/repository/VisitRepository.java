package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
