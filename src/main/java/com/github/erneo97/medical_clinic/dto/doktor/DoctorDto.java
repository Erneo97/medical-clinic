package com.github.erneo97.medical_clinic.dto.doktor;

import com.github.erneo97.medical_clinic.model.Specialization;

public record DoctorDto(
        Long id,
        Long userId,
        String email,
        String firstName,
        String lastName,
        Specialization specialization
) {
}
