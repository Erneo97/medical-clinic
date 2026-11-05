package com.github.erneo97.medical_clinic.facade;

import com.github.erneo97.medical_clinic.dto.BookingSummaryDto;
import com.github.erneo97.medical_clinic.mapper.DoctorMapper;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.model.Visit;
import com.github.erneo97.medical_clinic.service.DoctorService;
import com.github.erneo97.medical_clinic.service.PatientService;
import com.github.erneo97.medical_clinic.service.VisitService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class VisitBookingFacade {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final VisitService visitService;

    private final VisitMapper visitMapper;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;

    @Transactional
    public BookingSummaryDto book(Long visitId, Long patientId) {
        Visit visit = visitService.bookVisit(visitId, patientId);
        Patient patient = patientService.findById(patientId);
        Doctor doctor = doctorService.getDoctorById(visit.getDoctor().getId());
        LocalDate dateVisit = visit.getStartsAt().toLocalDate();
        return BookingSummaryDto.builder()
                .visit(visitMapper.toVisit(visit))
                .doctor(doctorMapper.toDto(doctor))
                .patient(patientMapper.toDto(patient))
                .freeSlotsThatDay(visitService.countFreeSlots(doctor.getId(), dateVisit))
                .build();
    }
}
