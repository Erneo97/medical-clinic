package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class VisitAlreadyBookedException extends  MedicalClinicException {
    public VisitAlreadyBookedException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
