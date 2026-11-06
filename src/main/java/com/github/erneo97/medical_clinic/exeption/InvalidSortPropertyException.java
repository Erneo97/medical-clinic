package com.github.erneo97.medical_clinic.exeption;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidSortPropertyException extends MedicalClinicException {
    public InvalidSortPropertyException(String property, List<String> allowed) {
        super(HttpStatus.BAD_REQUEST,
                String.format("cannot sort by '%s', allowed: %s", property, String.join(", ", allowed))
        );
        ;
    }
}
