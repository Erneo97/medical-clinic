package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends MedicalClinicException {
    private final static String messageFormat = "Patient not found with id %s";

    public PatientNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format(messageFormat, id.toString()));
    }

    public PatientNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, email);
    }
}
