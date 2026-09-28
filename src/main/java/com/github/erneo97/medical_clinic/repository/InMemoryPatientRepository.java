package com.github.erneo97.medical_clinic.repository;

import com.github.erneo97.medical_clinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPatientRepository {
    private final int OFFSET = 1;
    private final AtomicLong counter = new AtomicLong(OFFSET);
    private final List<Patient> patients = new ArrayList<>();

    public List<Patient> findAll() {return List.copyOf(patients);}

    public Optional<Patient> findById(Long id) {
        return patients.stream()
                .filter(patient -> patient.getId().equals(id))
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

    public void changePassword(long id, String newPassword) {
        patients.get((int)id - OFFSET).setPassword(newPassword);
    }

    public boolean deleteById(Long id) {
        return patients.removeIf(patient -> Objects.equals(patient.getId(), id));
    }

    public Patient update(long id, Patient patient) {
        int innerId = (int)id - OFFSET;
        patients.get(innerId).setFirstName(patient.getFirstName());
        patients.get(innerId).setLastName(patient.getLastName());
        patients.get(innerId).setBirthday(patient.getBirthday());
        patients.get(innerId).setEmail(patient.getEmail());
        patients.get(innerId).setPassword(patient.getPassword());
        patients.get(innerId).setIdCardNo(patient.getIdCardNo());
        patients.get(innerId).setPhoneNumber(patient.getPhoneNumber());
        return patient;
    }
}
