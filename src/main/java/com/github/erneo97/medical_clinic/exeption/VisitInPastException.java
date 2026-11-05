package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class VisitInPastException extends MedicalClinicException{
    public VisitInPastException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
