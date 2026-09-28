package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.repository.InMemoryPatientRepository;
import com.github.erneo97.medical_clinic.service.exception.PatientAlreadyExistsException;
import com.github.erneo97.medical_clinic.service.exception.PatientNotExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final InMemoryPatientRepository inMemoryPatientRepository;

    public List<Patient> findAll() {
        return inMemoryPatientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {
        return inMemoryPatientRepository.findById(id);
    }

    public Optional<Patient> findByEmail(String email) {
        return inMemoryPatientRepository.findByEmail(email);
    }

    public Patient create(Patient patient) {
        if (inMemoryPatientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new PatientAlreadyExistsException(
                    "Patient with email " + patient.getEmail() + " already exists"
            );
        }
        return inMemoryPatientRepository.save(patient);
    }

    public Optional<Patient> update(Long id, Patient patient) {
        Optional<Patient> optionalPatient = inMemoryPatientRepository.findById(id);
        return optionalPatient.map(value -> inMemoryPatientRepository.update(value.getId(), patient));
    }

    public void chanePassword(Long id, Map<String, String> password) {
        Patient patient =  inMemoryPatientRepository.findById(id).orElseThrow( () -> new PatientNotExists("Patient with id " + id + " does not exist") );
        inMemoryPatientRepository.changePassword(id, password.get("password"));
    }

    public boolean removeById(Long id) {
        return inMemoryPatientRepository.deleteById(id);
    }
}
