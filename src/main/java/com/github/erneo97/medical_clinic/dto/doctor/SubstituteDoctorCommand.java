package com.github.erneo97.medical_clinic.dto.doctor;

import jakarta.validation.constraints.NotNull;

public record SubstituteDoctorCommand(
        @NotNull(message = "substitute id is required")
        Long substituteId
) {
}
