package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndStartsAtLessThanAndEndsAtGreaterThan(
            Long doctorId,
            LocalDateTime newEnd,
            LocalDateTime newStart);
}
