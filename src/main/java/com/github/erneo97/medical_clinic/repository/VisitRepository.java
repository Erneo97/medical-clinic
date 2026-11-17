package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByDoctorId(Long doctorId);

    List<Visit> findByPatientId(Long patientId);

    boolean existsByDoctorIdAndStartsAtBeforeAndEndsAtAfter(
            Long doctorId,
            LocalDateTime newEnd,
            LocalDateTime newStart);

    @Query("""
            SELECT COUNT(v)
            FROM Visit v
            WHERE v.doctor.id = :doctorId AND v.patient IS NULL
              AND v.startsAt >= :start AND v.startsAt < :end
            """)
    long countFreeSlots(@Param("doctorId") Long doctorId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
