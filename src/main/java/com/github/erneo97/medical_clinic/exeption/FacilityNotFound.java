package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class FacilityNotFound extends MedicalClinicException {
    private final static String messageFormat = "Facility not found with id %s";

    public FacilityNotFound(Long id) {
        super(HttpStatus.NOT_FOUND, String.format(messageFormat, id.toString()));
    }
}
