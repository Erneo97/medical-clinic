package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.exeption.*;
import com.github.erneo97.medical_clinic.facade.UserFacade;
import com.github.erneo97.medical_clinic.facade.VisitBookingFacade;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.model.Visit;
import com.github.erneo97.medical_clinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

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

    @MockitoBean
    private UserFacade userFacade;

    private VisitService visitService;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        VisitMapper visitMapper = Mappers.getMapper(VisitMapper.class);
        visitService = new VisitService(visitRepository, visitMapper, userFacade, clock);
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

    @Test
    void bookVisit_visitNotExists_throwsVisitNotFoundException() {
        // given
        Long visitId = 1L;
        Long patientId = 2L;
        when(visitRepository.findById(visitId)).thenReturn(Optional.empty());

        // when
        VisitNotFoundException exception = assertThrows(VisitNotFoundException.class, () -> visitService.bookVisit(visitId, patientId));

        // then
        assertEquals(
                "Visit with id 1 not exist",
                exception.getMessage()
        );
        verify(patientService, never()).findById(any());
    }

    @Test
    void bookVisit_patientNotExists_throwsPatientNotFoundException() {
        // given
        Long visitId = 1L;
        Long patientId = 2L;
        Visit visit = createFutureVisit();

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(patientService.findById(patientId)).thenThrow(new PatientNotFoundException(patientId));

        // when
        assertThrows(
                PatientNotFoundException.class,
                () -> visitService.bookVisit(visitId, patientId)
        );

        // then
        assertNull(visit.getPatient());
    }

    @Test
    void bookVisit_visitAlreadyBooked_throwsVisitAlreadyBookedException() {
        // given
        Long visitId = 1L;
        Long patientId = 2L;
        Patient existingPatient = new Patient();
        Patient newPatient = new Patient();

        Visit visit = createFutureVisit();
        visit.setPatient(existingPatient);

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));

        when(patientService.findById(patientId)).thenReturn(newPatient);

        // when
        VisitAlreadyBookedException exception = assertThrows(VisitAlreadyBookedException.class, () -> visitService.bookVisit(visitId, patientId)
        );

        // then
        assertEquals("This visit is booked", exception.getMessage());
        assertSame(existingPatient, visit.getPatient());
    }

    @Test
    void bookVisit_visitInPast_throwsVisitInPastException() {
        // given
        Long visitId = 1L;
        Long patientId = 2L;
        Patient patient = new Patient();
        Visit visit = new Visit();
        visit.setStartsAt(NOW.minusHours(1));
        visit.setEndsAt(NOW.minusMinutes(15));

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(patientService.findById(patientId)).thenReturn(patient);

        // when
        VisitInPastException exception = assertThrows(
                VisitInPastException.class,
                () -> visitService.bookVisit(visitId, patientId)
        );

        // then
        assertEquals("The visit has already taken place", exception.getMessage());
        assertNull(visit.getPatient());
    }

    @Test
    void bookVisit_visitAvailable_assignsPatient() {
        // given
        Long visitId = 1L;
        Long patientId = 2L;
        Patient patient = new Patient();
        Visit visit = createFutureVisit();

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(patientService.findById(patientId)).thenReturn(patient);

        // when
        Visit result = visitService.bookVisit(visitId, patientId);

        // then
        assertSame(patient, result.getPatient());
        assertSame(visit, result);
    }

    private Visit createFutureVisit() {
        Visit visit = new Visit();

        visit.setStartsAt(NOW.plusHours(1));
        visit.setEndsAt(NOW.plusHours(1).plusMinutes(45));

        return visit;
    }

    @Test
    void findAll_allowedSort_returnsRepositoryPage() {
        // given
        PageRequest pageable = PageRequest.of(1, 5, Sort.by("startsAt").descending());
        Page<Visit> page =
                new PageImpl<>(List.of(new Visit()), pageable, 12);
        when(visitRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<Visit> result = visitService.findAll(pageable);

        // then
        assertSame(page, result);
    }

    @Test
    void findAll_unknownSortProperty_throwsInvalidSortProperty() {
        // given
        PageRequest pageable = PageRequest.of(0, 20, Sort.by("abrakadabra"));

        // when + then
        InvalidSortPropertyException exception = assertThrows(
                InvalidSortPropertyException.class, () -> visitService.findAll(pageable));
        assertEquals("cannot sort by 'abrakadabra', allowed: startsAt, endsAt",
                exception.getMessage());
        verify(visitRepository, never()).findAll(any(PageRequest.class));
    }
}