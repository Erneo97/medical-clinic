package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class PatientNotFound extends MedicalClinicException {
    private static String messageFormat = "Patient not found with id %s";

    public PatientNotFound(Long id) {
        super(HttpStatus.NOT_FOUND, String.format(messageFormat, id.toString()));
    }

    public PatientNotFound(String email) {
        super(HttpStatus.NOT_FOUND, email);
    }
}
