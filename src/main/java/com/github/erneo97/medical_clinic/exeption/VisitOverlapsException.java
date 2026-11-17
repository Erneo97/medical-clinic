package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class VisitOverlapsException extends MedicalClinicException {
    public VisitOverlapsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
