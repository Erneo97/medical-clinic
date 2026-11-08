package com.github.erneo97.medical_clinic.facade;

import com.github.erneo97.medical_clinic.dto.BookingSummaryDto;
import com.github.erneo97.medical_clinic.mapper.DoctorMapper;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.model.*;
import com.github.erneo97.medical_clinic.service.DoctorService;
import com.github.erneo97.medical_clinic.service.PatientService;
import com.github.erneo97.medical_clinic.service.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitBookingFacadeTest {
    private static LocalDateTime NOW = LocalDateTime.of(2030, 1, 1, 8, 0);
    @Mock
    private PatientService patientService;
    @Mock
    private DoctorService doctorService;
    @Mock
    private VisitService visitService;
    @MockitoBean
    private Clock clock;

    private VisitBookingFacade facasde;

    @BeforeEach
    void setUp() {
        facasde = new VisitBookingFacade(patientService, doctorService, visitService,
                Mappers.getMapper(VisitMapper.class),
                Mappers.getMapper(DoctorMapper.class),
                Mappers.getMapper(PatientMapper.class));
    }


    @Test
    void book_freeVisits_booksFirstThenBuildsSummary() {
        // given
        Long visitId = 3L, patientId = 2L, doctorId = 1L;

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .firstName("Jan")
                .lastName("Kowalski")
                .specialization(Specialization.PEDIATRICIAN).build();
        Patient patient = Patient.builder()
                .id(patientId)
                .firstName("Ewa")
                .lastName("Nowak")
                .user(User.builder().email("ewa.nowak@exp.net").build())
                .build();

        LocalDateTime visitStartsAt =NOW.plusDays(1);

        Visit visit = Visit.builder()
                .id(visitId).
                startsAt(visitStartsAt)
                .doctor(doctor)
                .endsAt(visitStartsAt.plusMinutes(45L))
                .build();

        when(visitService.bookVisit(visitId,patientId )).thenReturn(visit);
        when(patientService.findById(patientId)).thenReturn(patient);
        when(doctorService.getDoctorById(doctorId)).thenReturn(doctor);
        when(visitService.countFreeSlots(doctorId, visitStartsAt.toLocalDate())).thenReturn(4L);

        // when
        BookingSummaryDto summaryDto = facasde.book(visitId, patientId);

        // then
        InOrder inOrder = Mockito.inOrder(visitService);
        inOrder.verify(visitService).bookVisit(visitId,patientId);
        inOrder.verify(visitService).countFreeSlots(doctorId, visitStartsAt.toLocalDate());

        assertEquals("Ewa Nowak", String.format("%s %s", summaryDto.patient().firstName(), summaryDto.patient().lastName()));
        assertEquals("Kowalski", summaryDto.doctor().lastName());
        assertEquals(4L, summaryDto.freeSlotsThatDay());
    }
}