package com.github.erneo97.medical_clinic.dto.doktor;

import com.github.erneo97.medical_clinic.model.Role;

public record UserDto(Long id, String email, Role role) {
}
