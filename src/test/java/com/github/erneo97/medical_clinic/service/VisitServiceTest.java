package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.exeption.DoctorNotFoundException;
import com.github.erneo97.medical_clinic.exeption.InvalidVisitException;
import com.github.erneo97.medical_clinic.exeption.VisitOverlapsException;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Visit;
import com.github.erneo97.medical_clinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
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
        Clock clock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
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

    @Test
    void create_startNotOnFullQuarterHour_throwsInvalidVisitException() {
        // given
        VisitCreateCommand command = new VisitCreateCommand(1L, NOW.plusMinutes(20), NOW.plusMinutes(30));
        when(doctorService.existsById(1L)).thenReturn(true);

        // when
        InvalidVisitException exception = assertThrows(InvalidVisitException.class, () -> visitService.create(command));

        // then
        assertEquals("a session must start on a full quarter-hour", exception.getMessage());
        verify(visitRepository, never()).save(any());
    }

    @Test
    void create_durationNotMultipleOf15Minutes_throwsInvalidVisitException() {
        // given
        VisitCreateCommand command = new VisitCreateCommand(1L, NOW.plusMinutes(15), NOW.plusMinutes(40));

        when(doctorService.existsById(1L)).thenReturn(true);

        // when
        InvalidVisitException exception = assertThrows(
                InvalidVisitException.class,
                () -> visitService.create(command)
        );

        // then
        assertEquals("session length must be a positive multiple of 15 minutes", exception.getMessage());
        verify(visitRepository, never()).save(any());
    }

    @Test
    void create_doctorNotExists_throwsDoctorNotFoundException() {
        // given
        VisitCreateCommand command = new VisitCreateCommand(1L, NOW.plusMinutes(15), NOW.plusMinutes(45));

        when(doctorService.existsById(1L)).thenReturn(false);

        // when
        DoctorNotFoundException exception = assertThrows(
                DoctorNotFoundException.class,
                () -> visitService.create(command)
        );

        // then
        assertEquals("Doctor Not Found with ID: 1", exception.getMessage());

        verify(visitRepository, never()).save(any());
    }

    @Test
    void create_visitOverlaps_throwsVisitOverlapsException() {
        // given
        VisitCreateCommand command = new VisitCreateCommand(1L, NOW.plusMinutes(15), NOW.plusMinutes(45));

        when(doctorService.existsById(1L)).thenReturn(true);

        when(visitRepository.existsByDoctorIdAndStartsAtBeforeAndEndsAtAfter(1L, command.endsAt(), command.startsAt())).thenReturn(true);

        // when
        VisitOverlapsException exception = assertThrows(
                VisitOverlapsException.class,
                () -> visitService.create(command)
        );

        // then
        assertTrue(exception.getMessage().contains("Visit already exists"));

        verify(visitRepository, never()).save(any());
        verify(doctorService, never()).findDoctorByIdOrThrowException(any());
    }

    @Test
    void create_startExactlyNow_savesVisitWithCorrectData() {
        // given
        Long doctorId = 1L;

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        VisitCreateCommand command = new VisitCreateCommand(doctorId, NOW, NOW.plusMinutes(45));

        when(doctorService.existsById(doctorId)).thenReturn(true);
        when(visitRepository.existsByDoctorIdAndStartsAtBeforeAndEndsAtAfter(doctorId, command.endsAt(), command.startsAt())).thenReturn(false);
        when(doctorService.findDoctorByIdOrThrowException(doctorId)).thenReturn(doctor);
        when(visitRepository.save(any(Visit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Visit result = visitService.create(command);

        // then
        ArgumentCaptor<Visit> captor =
                ArgumentCaptor.forClass(Visit.class);

        verify(visitRepository).save(captor.capture());

        Visit savedVisit = captor.getValue();

        assertEquals(NOW, savedVisit.getStartsAt());
        assertEquals(NOW.plusMinutes(45), savedVisit.getEndsAt());
        assertSame(doctor, savedVisit.getDoctor());

        assertSame(savedVisit, result);
    }
}