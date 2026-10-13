package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.PatientCreateCommand;
import com.github.erneo97.medical_clinic.dto.PatientDto;
import com.github.erneo97.medical_clinic.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PatientMapper {
     @Mapping(target = "email", source = "patient", qualifiedByName = "patientEmail")
     PatientDto toDto(Patient patient);

     List<PatientDto> toDto(List<Patient> patients);

     @Mapping(target = "id", ignore = true)
     @Mapping(source = "email", target = "user.email")
     Patient toPatient(PatientCreateCommand command);

     @Mapping(source = "email", target = "user.email")
     @Mapping(target = "id", ignore = true)
     @Mapping(target = "idCardNo", ignore = true)
     Patient toPatient(EditPersonalDataCommand command);

     @Named("patientEmail")
     default String getEmailFromPatient(Patient patient) {
          return patient.getEmail();
     }


}
