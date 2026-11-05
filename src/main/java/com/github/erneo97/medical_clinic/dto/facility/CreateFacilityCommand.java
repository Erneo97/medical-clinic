package com.github.erneo97.medical_clinic.dto.facility;

import jakarta.validation.constraints.NotBlank;

public record CreateFacilityCommand (
        @NotBlank(message = "field is required")
        String name,

        @NotBlank(message = "field is required")
        String city,

        @NotBlank(message = "field is required")
        String zipCode,

        @NotBlank(message = "field is required")
        String street,

        @NotBlank(message = "field is required")
        int number
){
}
