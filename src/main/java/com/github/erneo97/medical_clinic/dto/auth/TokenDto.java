package com.github.erneo97.medical_clinic.dto.auth;

public record TokenDto(
        String token,
        String tokenType,
        int expiresIn
) {
}
