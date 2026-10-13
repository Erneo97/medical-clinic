package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.doktor.UserCreateCommand;
import com.github.erneo97.medical_clinic.dto.doktor.UserDto;
import com.github.erneo97.medical_clinic.dto.patient.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.patient.PatientCreateCommand;
import com.github.erneo97.medical_clinic.dto.patient.PatientDto;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.model.User;
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

     @Mapping(target = "role", ignore = true)
     @Mapping(target = "id", ignore = true)
     User toUser(PatientCreateCommand command);

     @Mapping(target = "id", ignore = true)
     User toUser(UserCreateCommand command);

     UserDto toUserDto(User user);

     @Named("patientEmail")
     default String getEmailFromPatient(Patient patient) {
          return patient.getEmail();
     }
}
