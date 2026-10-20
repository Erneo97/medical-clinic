package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.patient.EditPasswordCommand;
import com.github.erneo97.medical_clinic.dto.patient.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.patient.PatientCreateCommand;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.mapper.UserMapper;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.model.Role;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.PatientRepository;
import com.github.erneo97.medical_clinic.exeption.PatientAlreadyExistsException;
import com.github.erneo97.medical_clinic.exeption.PatientNotFound;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;
    private final UserMapper userMapper;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFound(id));
    }

    public Patient findByEmail(String email) {
        return patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFound(email));
    }

    @Transactional
    public Patient create(PatientCreateCommand command) {
        // Walidacja biznesowa: sprawdzamy, czy email nie jest już zajęty.
        // Wymaga dostępu do innych pacjentów, dlatego znajduje się w serwisie.
        // Walidację formalną danych wejściowych wykonuje już @Valid.
        if (userRepository.existsByEmail(command.email())) {
            throw new PatientAlreadyExistsException(
                    "Patient with email " + command.email() + " already exists",
                    HttpStatus.CONFLICT
            );
        }
        User newUser = User.builder()
                .email(command.email())
                .password(command.password())
                .role(Role.PATIENT)
                .build();

        Patient patient = patientMapper.toPatient(command);
        patient.setUser(newUser);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient update(Long id, EditPersonalDataCommand command) {
        Patient patient = patientRepository.findById(id)
                        .orElseThrow(() -> new PatientNotFound(id));
        patient.updatePatient(command);
        return patient;
    }

    public void chanePassword(Long id, EditPasswordCommand command) {
        patientRepository.findById(id).orElseThrow( () -> new PatientNotFound(id) );
        patientRepository.updatePassword(id, command.password());
    }

    public void removeById(Long id) {
        findUserByIdOrThrowException(id);
        patientRepository.deleteById(id);
    }

    public List<Patient> searchBytName(String fragment) {
        return patientRepository.searchByName(fragment);
    }

    private void findUserByIdOrThrowException(Long id) {
        this.findById(id);
    }
}
