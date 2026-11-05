package com.github.erneo97.medical_clinic.dto.doktor;

import com.github.erneo97.medical_clinic.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateCommand (
    @NotBlank(message = "email is required")
    @Email(message = "this does not look like an email address")
    String email,

    @NotBlank(message = "email is required")
    @Size(min = 8, message = "password must be at least 8 characters long")
    String password,

    Role role

    ) {}
