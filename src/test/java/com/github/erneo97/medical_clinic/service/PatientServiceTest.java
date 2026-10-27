package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.patient.EditPasswordCommand;
import com.github.erneo97.medical_clinic.dto.patient.PatientCreateCommand;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.PatientRepository;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void findById_patientExists_returnsPatient() {
        // given
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Jan")
                .build();
        when(patientRepository.findById(1L))
                .thenReturn(Optional.ofNullable(patient));

        // when
        Patient result = patientService.findById(1L);

        // then
        assertEquals("Jan", result.getFirstName());
        assertEquals(1L, result.getId());
    }

    @Test
    public void findByEmail_patientExists_returnsPatient() {
        // given
        String searchEmail = "jan.testowy@testowy.pl";
        Patient patient = this.getPatientWithEmail(searchEmail);
        when(patientRepository.findByUserEmail(searchEmail))
                .thenReturn(Optional.ofNullable(patient));

        // when
        Patient result = patientService.findByEmail(searchEmail);

        // then
        assertEquals(searchEmail, result.getEmail());
    }

    @Test
    public void findAll_twoPatientsExists_returnsBoth() {
        // given
        Patient p1 = Patient.builder().id(1L).firstName("Jan").build();
        Patient p2 = Patient.builder().id(2L).firstName("Anna").build();
        when(patientRepository.findAll())
                .thenReturn(List.of(p1, p2));

        // when
        List<Patient> result = patientService.findAll();

        // then
        assertEquals(2, result.size());
        assertEquals("Jan", result.get(0).getFirstName());
        assertEquals("Anna", result.get(1).getFirstName());
    }

    @Test
    public void searchByName_patientWithPartLastnameExists_returnsPatients() {
        // given
        Patient p1 = Patient.builder().id(1L).lastName("Antracyt").build();
        Patient p2 = Patient.builder().id(2L).lastName("Antropolog").build();

        when(patientService.searchByName("an"))
                .thenReturn(List.of(p1, p2));
        // when
        List<Patient> result = patientService.searchByName("an");

        // then
        assertEquals(2, result.size());
    }

    @Test
    void create_newEmail_savesPatientWithAccount() {
        // given
        String email = "test@test.test";
        PatientCreateCommand command = PatientCreateCommand.builder().email(email).build();
        Patient mapped = getPatientWithEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(patientMapper.toPatient(command)).thenReturn(mapped);
        when(patientRepository.save(mapped)).thenReturn(mapped);

        // when
        Patient result = patientService.create(command);

        // then
        assertEquals(email, result.getEmail());
        verify(patientRepository).save(mapped);
    }

    @Test
    public void changePassword_patientExists_setsNewPassword() {
        // given
        final String oldPassword = "oldPassword123!",
                newPassword = "newPassword123!";
        Patient patient = getPatientWithPassword(oldPassword);
        EditPasswordCommand editPasswordCommand = new EditPasswordCommand(newPassword);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(getPatientWithPassword(newPassword)));


        // when
        patientService.chanePassword(1L, editPasswordCommand);
        Patient result = patientService.findById(1L);

        // then
        verify(patientRepository).updatePassword(1L, newPassword);
    }

    private Patient getPatientWithPassword(final String password) {
        User account = User.builder().password(password).build();
        return Patient.builder().id(1L).user(account).build();
    }

    private Patient getPatientWithEmail(final String email) {
        User account = User.builder()
                .email(email)
                .build();

        return Patient.builder()
                .user(account)
                .id(1L)
                .build();
    }
}