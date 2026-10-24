package com.github.erneo97.medical_clinic.service;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void findById_patientExists_returnsPatient() {
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
    void findByEmail_patientExists_returnsPatient() {
        // given
        String searchEmail = "jan.testowy@testowy.pl";
        Patient patient  = this.getPatientWithEmail(searchEmail);
        when(patientRepository.findByUserEmail(searchEmail))
                .thenReturn(Optional.ofNullable(patient));

        // when
        Patient result = patientService.findByEmail(searchEmail);

        // then
        assertEquals(searchEmail, result.getEmail());
    }

    @Test
    void findAll_twoPatients_returnsBoth() {
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

    private Patient getPatientWithEmail(String email) {
        User  user = User.builder()
                .email(email)
                .build();

        return Patient.builder()
                .user(user)
                .id(1L)
                .build();
    }
}