package com.github.erneo97.medical_clinic.dto.visit;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record VisitCreateCommand(
        @NotBlank(message = "field is required")
        Long doctorId,

        @NotBlank(message = "field is required")
        @Future(message = "date must be in the future")
        LocalDateTime startsAt,

        @NotNull(message = "field is required")
        @Future(message = "date must be in the future")
        LocalDateTime endsAt
) {
}
