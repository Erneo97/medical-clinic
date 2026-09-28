package com.github.erneo97.medical_clinic.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotExists extends RuntimeException {
    public PatientNotExists(String message) {
        super(message);
    }
}
