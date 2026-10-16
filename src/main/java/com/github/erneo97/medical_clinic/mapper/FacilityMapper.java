package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.FacilityDto;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Facility;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FacilityMapper {
    @Mapping(target = "doctors", source = "facility.doctors", qualifiedByName = "doctorNames")
    FacilityDto mapToDto(Facility facility);

    List<FacilityDto> mapToDto(List<Facility> facility);

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
