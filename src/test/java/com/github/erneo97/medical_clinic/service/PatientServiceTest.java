package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.repository.PatientRepository;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    }

    @Test
    void create_patientWasCreated_returnsPatient() {
    }
}