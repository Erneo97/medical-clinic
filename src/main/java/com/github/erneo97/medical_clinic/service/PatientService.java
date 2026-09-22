package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.repository.InMemoryPatientRepository;
import com.github.erneo97.medical_clinic.service.exception.PatientAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final InMemoryPatientRepository inMemoryPatientRepository;

    public List<Patient> findAll() {return inMemoryPatientRepository.findAll();}

    public Optional<Patient> findById(int id) {
        return inMemoryPatientRepository.findById(id);
    }

    public void create(Patient patient) {
        inMemoryPatientRepository.findByEmail(patient.getEmail()).orElseThrow(() -> new PatientAlreadyExistsException("Patient with email " + patient.getEmail() + " already exists"));
        inMemoryPatientRepository.save(patient);
    }
}
