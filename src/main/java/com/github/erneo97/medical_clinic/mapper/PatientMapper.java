package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.PatientCreateCommand;
import com.github.erneo97.medical_clinic.dto.PatientDto;
import com.github.erneo97.medical_clinic.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PatientMapper {
     PatientDto toDto(Patient patient);

     List<PatientDto> toDto(List<Patient> patients);

     @Mapping(target = "id", ignore = true)
     Patient toDtoCreate(PatientCreateCommand command);
}
