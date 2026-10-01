package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.EditPasswordCommand;
import com.github.erneo97.medical_clinic.dto.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.PatientCreateCommand;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.repository.InMemoryPatientRepository;
import com.github.erneo97.medical_clinic.exeption.PatientAlreadyExistsException;
import com.github.erneo97.medical_clinic.exeption.PatientNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public Patient findById(Long id) {
        return inMemoryPatientRepository.findById(id).orElseThrow(() -> new PatientNotFound(id));
    }

    public Patient findByEmail(String email) {
        return inMemoryPatientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound(email));
    }

    public Patient create(PatientCreateCommand command) {
        // Walidacja biznesowa: sprawdzamy, czy email nie jest już zajęty.
        // Wymaga dostępu do innych pacjentów, dlatego znajduje się w serwisie.
        // Walidację formalną danych wejściowych wykonuje już @Valid.
        if (inMemoryPatientRepository.findByEmail(command.email()).isPresent()) {
            throw new PatientAlreadyExistsException(
                    "Patient with email " + command.email() + " already exists",
                    HttpStatus.CONFLICT
            );
        }
        Patient patient = patientMapper.toPatient(command);
        return inMemoryPatientRepository.save(patient);
    }

    public Patient update(Long id, EditPersonalDataCommand command) {
        this.findById(id);
        Patient patientNewData = patientMapper.toPatient(command);
        return inMemoryPatientRepository.update(id, patientNewData);
    }

    public void chanePassword(Long id, EditPasswordCommand command) {
        inMemoryPatientRepository.findById(id).orElseThrow( () -> new PatientNotFound(id) );
        inMemoryPatientRepository.changePassword(id, command.password());
    }

    public boolean removeById(Long id) {
        return inMemoryPatientRepository.deleteById(id);
    }
}
