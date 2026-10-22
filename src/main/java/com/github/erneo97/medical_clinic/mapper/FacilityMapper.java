package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.facility.CreateFacilityCommand;
import com.github.erneo97.medical_clinic.dto.facility.FacilityDto;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Facility;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FacilityMapper {

    FacilityDto mapToDto(Facility facility);

    List<FacilityDto> mapToDto(List<Facility> facility);

    @Mapping(target = "id", ignore = true)
    Facility mapToEntity(CreateFacilityCommand command);

    @Named("doctorNames")
    default List<String> doctorNames(Set<Doctor> doctors) {
        return doctors.stream()
                 .map(this::getFullName)
                 .toList();
    }

    private String getFullName(Doctor doctor) {
        return doctor.getFirstName() + " " + doctor.getLastName();
    }
}
