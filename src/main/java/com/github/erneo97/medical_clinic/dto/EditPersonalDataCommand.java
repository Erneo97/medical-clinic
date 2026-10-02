package com.github.erneo97.medical_clinic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record EditPersonalDataCommand(
        @NotBlank(message = "email is required")
        @Email(message = "this does not look like an email address")
        String email,

        @NotBlank(message = "email is required")
        String firstName,

        @NotBlank(message = "email is required")
        String lastName,

        @NotBlank(message = "email is required")
        @Pattern(regexp = "\\d{9}", message = "phone number must have 9 digits")
        String phoneNumber,

        @Past(message = "birth date must be in the past")
        LocalDate birthday
) {
}
