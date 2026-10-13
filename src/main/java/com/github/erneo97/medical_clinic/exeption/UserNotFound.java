package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

public class UserNotFound extends MedicalClinicException {
    private static String messageFormat = "User not found with id %s";

    public UserNotFound(Long id) {
        super(HttpStatus.NOT_FOUND, String.format(messageFormat, id.toString()));
    }

    public UserNotFound(String email) {
        super(HttpStatus.NOT_FOUND, email);
    }
}
