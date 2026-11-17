package com.github.erneo97.medical_clinic.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginCommand(
        @NotBlank(message = "field is required")
        @Email
        String email,

        @NotBlank(message = "field is required")
        String password
) {
}
