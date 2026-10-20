package com.github.erneo97.medical_clinic.dto.doctor;

import com.github.erneo97.medical_clinic.model.Specialization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorCreateCommand(
        @NotNull(message = "field is required")
        Long userId,

        @NotBlank(message = "field is required")
        String firstName,

        @NotBlank(message = "field is required")
        String lastName,

        @NotBlank(message = "field is required")
        Specialization specialization
) {
}
