package com.github.erneo97.medical_clinic.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PatientCreateCommand (
        @NotBlank(message = "email is required")
        @Email(message = "this does not look like an email address")
        String email,

        @NotBlank(message = "email is required")
        @Size(min = 8, message = "password must be at least 8 characters long")
        String password,

        @NotBlank(message = "card number is required")
        @Pattern(regexp = "[A-Z]{3}\\d{6}", message = "card number must look like ABC123456")
        String idCardNo,

        @NotBlank(message = "email is required")
        String firstName,

        @NotBlank(message = "email is required")
        String lastName,

        @NotBlank(message = "email is required")
        @Pattern(regexp = "\\d{9}", message = "phone number must have 9 digits")
        String phoneNumber,

        @Past(message = "birth date must be in the past")
        LocalDate birthday
){
}
