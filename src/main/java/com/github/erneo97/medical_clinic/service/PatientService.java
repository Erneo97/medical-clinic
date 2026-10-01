package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.EditPasswordCommand;
import com.github.erneo97.medical_clinic.dto.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.PatientCreateCommand;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.repository.InMemoryPatientRepository;
import com.github.erneo97.medical_clinic.service.exception.PatientAlreadyExistsException;
import com.github.erneo97.medical_clinic.service.exception.PatientNotExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final InMemoryPatientRepository inMemoryPatientRepository;
    private final PatientMapper patientMapper;

    public List<Patient> findAll() {
        return inMemoryPatientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {
        return inMemoryPatientRepository.findById(id);
    }

    public Optional<Patient> findByEmail(String email) {
        return inMemoryPatientRepository.findByEmail(email);
    }

    public Patient create(PatientCreateCommand command) {
        // Walidacja biznesowa: sprawdzamy, czy email nie jest już zajęty.
        // Wymaga dostępu do innych pacjentów, dlatego znajduje się w serwisie.
        // Walidację formalną danych wejściowych wykonuje już @Valid.
        if (inMemoryPatientRepository.findByEmail(command.email()).isPresent()) {
            throw new PatientAlreadyExistsException(
                    "Patient with email " + command.email() + " already exists"
            );
        }
        Patient patient = patientMapper.toPatient(command);
        return inMemoryPatientRepository.save(patient);
    }

    public Optional<Patient> update(Long id, EditPersonalDataCommand command) {
        Optional<Patient> optionalPatient = inMemoryPatientRepository.findById(id);
        return optionalPatient.map(patientToUpdate -> {
            Patient patientNewData = patientMapper.toPatient(command);
            return inMemoryPatientRepository.update(id, patientNewData);
        });
    }

    public void chanePassword(Long id, EditPasswordCommand command) {
        inMemoryPatientRepository.findById(id).orElseThrow( () -> new PatientNotExists("Patient with id " + id + " does not exist") );
        inMemoryPatientRepository.changePassword(id, command.password());
    }

    public boolean removeById(Long id) {
        return inMemoryPatientRepository.deleteById(id);
    }
}
