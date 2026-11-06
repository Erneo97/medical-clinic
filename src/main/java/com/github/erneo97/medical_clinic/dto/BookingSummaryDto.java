package com.github.erneo97.medical_clinic.dto;

import com.github.erneo97.medical_clinic.dto.doctor.DoctorDto;
import com.github.erneo97.medical_clinic.dto.patient.PatientDto;
import com.github.erneo97.medical_clinic.dto.visit.VisitDto;
import lombok.Builder;

@Builder
public record BookingSummaryDto(
        VisitDto visit,
        PatientDto patient,
        DoctorDto doctor,
        long freeSlotsThatDay
) {
}
