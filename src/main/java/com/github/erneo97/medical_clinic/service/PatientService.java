package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.EditPasswordCommand;
import com.github.erneo97.medical_clinic.dto.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.PatientCreateCommand;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.repository.PatientRepository;
import com.github.erneo97.medical_clinic.exeption.PatientAlreadyExistsException;
import com.github.erneo97.medical_clinic.exeption.PatientNotFound;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFound(id));
    }

    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound(email));
    }

    public Patient create(PatientCreateCommand command) {
        // Walidacja biznesowa: sprawdzamy, czy email nie jest już zajęty.
        // Wymaga dostępu do innych pacjentów, dlatego znajduje się w serwisie.
        // Walidację formalną danych wejściowych wykonuje już @Valid.
        if (patientRepository.findByEmail(command.email()).isPresent()) {
            throw new PatientAlreadyExistsException(
                    "Patient with email " + command.email() + " already exists",
                    HttpStatus.CONFLICT
            );
        }
        Patient patient = patientMapper.toPatient(command);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient update(Long id, EditPersonalDataCommand command) {
        Patient patient = patientRepository.findById(id)
                        .orElseThrow(() -> new PatientNotFound(id));
        updatePatienData(patient, command);
        return patientRepository.save(patient);
    }

    public void chanePassword(Long id, EditPasswordCommand command) {
        patientRepository.findById(id).orElseThrow( () -> new PatientNotFound(id) );
        patientRepository.updatePassword(id, command.password());
    }

    public void removeById(Long id) {
        findUserByIdOrThrowException(id);
        patientRepository.deleteById(id);
    }

    private void findUserByIdOrThrowException(Long id) {
        this.findById(id);
    }

    private void updatePatienData(Patient patientToUpdate, EditPersonalDataCommand newData) {
        patientToUpdate.setEmail(newData.email());
        patientToUpdate.setFirstName(newData.firstName());
        patientToUpdate.setLastName(newData.lastName());
        patientToUpdate.setPhoneNumber(newData.phoneNumber());
        patientToUpdate.setBirthday(newData.birthday());
    }
}
