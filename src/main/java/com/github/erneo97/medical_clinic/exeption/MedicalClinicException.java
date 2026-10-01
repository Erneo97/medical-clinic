package com.github.erneo97.medical_clinic.exeption;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class MedicalClinicException extends RuntimeException {
    private final HttpStatus status;

    protected MedicalClinicException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}

