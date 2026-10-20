package com.github.erneo97.medical_clinic.dto.facility;

import java.util.List;

public record FacilityDto (
        Long id,
        String name,
        String city,
        String zipCode,
        String street,
        int number
) {
}
