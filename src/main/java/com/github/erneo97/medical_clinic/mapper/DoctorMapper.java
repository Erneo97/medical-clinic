package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.doctor.DoctorCreateCommand;
import com.github.erneo97.medical_clinic.dto.doctor.DoctorDto;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DoctorMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "facilities", source = "doctor.facilities", qualifiedByName = "facilityNames")
    DoctorDto toDto(Doctor doctor);

    List<DoctorDto> toDto(List<Doctor> doctor);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    Doctor toDoctor(DoctorCreateCommand command);

    @Named("facilityNames")
    default List<String> facilityNames(Set<Facility> facilities) {
        return facilities.stream()
                .map(Facility::getName)
                .sorted()
                .toList();
    }
}
