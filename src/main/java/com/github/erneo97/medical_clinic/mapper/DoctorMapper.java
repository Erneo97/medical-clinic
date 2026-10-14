package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.doctor.DoctorCreateCommand;
import com.github.erneo97.medical_clinic.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DoctorMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    DoctorCreateCommand.DoctorDto toDto(Doctor doctor);

    List<DoctorCreateCommand.DoctorDto> toDto(List<Doctor> doctor);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Doctor toDoctor(DoctorCreateCommand command);
}
