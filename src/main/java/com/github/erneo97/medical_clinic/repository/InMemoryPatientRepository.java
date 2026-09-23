package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPatientRepository {
    private final AtomicLong counter = new AtomicLong(1);
    private final List<Patient> patients = new ArrayList<>();

    public List<Patient> findAll() {return List.copyOf(patients);}

    public Optional<Patient> findById(int id) {
        return patients.stream()
                .filter(patient -> patient.getId() == id)
                .findFirst();
    }

    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public Patient save(Patient patient) {
        patient.setId(counter.getAndIncrement());
        patients.add(patient);
        return patient;
    }

    public boolean deleteById(int id) {
        return patients.removeIf(patient -> patient.getId() == id);
    }
}
