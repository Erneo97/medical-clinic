package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class PatientAlreadyExistsException extends MedicalClinicException {
    public PatientAlreadyExistsException(String message,  HttpStatus status) {
        super(status, message);
    }
}
