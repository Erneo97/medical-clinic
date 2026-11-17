package com.github.erneo97.medical_clinic.dto.patient;

import java.time.LocalDate;

public record PatientDto (
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday
) {}
