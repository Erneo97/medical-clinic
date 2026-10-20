package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class InvalidVisitException extends MedicalClinicException {
    protected InvalidVisitException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
