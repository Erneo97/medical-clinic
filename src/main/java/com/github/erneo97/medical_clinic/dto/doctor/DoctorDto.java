package com.github.erneo97.medical_clinic.dto.doctor;

import com.github.erneo97.medical_clinic.model.Facility;
import com.github.erneo97.medical_clinic.model.Specialization;

import java.util.Set;

public record DoctorDto(
        Long id,
        Long userId,
        String email,
        String firstName,
        String lastName,
        Specialization specialization,
        Set<Facility> facilities
) {
}
