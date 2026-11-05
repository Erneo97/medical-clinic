package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class FacilityNotFoundException extends MedicalClinicException {
    private final static String messageFormat = "Facility not found with id %s";

    public FacilityNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format(messageFormat, id.toString()));
    }
}
