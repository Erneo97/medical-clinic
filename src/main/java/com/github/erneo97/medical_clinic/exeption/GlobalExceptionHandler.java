package com.github.erneo97.medical_clinic.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicalClinicException.class)
    public ProblemDetail handleMedicalClinic(MedicalClinicException exception) {
        log.warn("błąd domeny {}",exception.getMessage());
        return ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
    }
}
