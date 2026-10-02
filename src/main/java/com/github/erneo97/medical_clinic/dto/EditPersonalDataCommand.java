package com.github.erneo97.medical_clinic.dto;

import java.time.LocalDate;

public record EditPersonalDataCommand(
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday
) {
}
