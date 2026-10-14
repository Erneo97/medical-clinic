package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.dto.visit.VisitDto;
import com.github.erneo97.medical_clinic.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VisitMapper {
    Visit toVisit(VisitCreateCommand command);

    List<VisitDto> toVisit(List<Visit> visits);
    VisitDto toVisit(Visit visit);
}
