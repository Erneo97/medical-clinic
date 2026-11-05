package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.dto.visit.VisitDto;
import com.github.erneo97.medical_clinic.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VisitMapper {
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "patientId", source = "patient.id")
    VisitDto toVisit(Visit visit);

    List<VisitDto> toVisit(List<Visit> visits);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "version", ignore = true)
    Visit toVisit(VisitCreateCommand command);

}
