package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class VisitNotFoundException extends MedicalClinicException {
    public VisitNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
