package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.exeption.InvalidVisitException;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2030, 1, 15, 8, 0);

    @Mock
    private VisitRepository visitRepository;
    @Mock
    private DoctorService doctorService;
    @Mock
    private PatientService patientService;

    private VisitService visitService;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        VisitMapper visitMapper = Mappers.getMapper(VisitMapper.class);
        visitService = new VisitService(visitRepository, visitMapper, doctorService, patientService, clock);
    }

    @Test
    void create_startInPast_throwsInvalidVisitException() {
        // given
        VisitCreateCommand command = new VisitCreateCommand(1L, NOW.minusMinutes(15), NOW.plusMinutes(45));
        when(doctorService.existsById(1L)).thenReturn(true);

        // when
        InvalidVisitException exception = assertThrows(InvalidVisitException.class, () -> visitService.create(command));

        // then
        assertEquals("Slot Start Date or Slot End Dates are in past", exception.getMessage());
        verify(visitRepository, never()).save(any());
    }
}