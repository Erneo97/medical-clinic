package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<Patient> findAll() {
        return patientService.findAll();
    }

    @PostMapping
    public Patient create(@RequestBody Patient patient) {
        return patientService.create(patient);
    }
}
