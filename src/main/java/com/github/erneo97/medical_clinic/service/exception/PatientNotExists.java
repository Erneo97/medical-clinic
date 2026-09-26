package com.github.erneo97.medical_clinic.service.exception;

public class PatientNotExists extends RuntimeException {
    public PatientNotExists(String message) {
        super(message);
    }
}
