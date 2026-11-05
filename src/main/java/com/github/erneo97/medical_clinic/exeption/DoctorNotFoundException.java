package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalClinicException{
    private final static String messageFormat = "Doctor not found with id %s";

    public DoctorNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format(messageFormat, id.toString()));
    }

    public DoctorNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, email);
    }
}
