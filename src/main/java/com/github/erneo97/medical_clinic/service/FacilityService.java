package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.facility.CreateFacilityCommand;
import com.github.erneo97.medical_clinic.exeption.FacilityNotFound;
import com.github.erneo97.medical_clinic.mapper.FacilityMapper;
import com.github.erneo97.medical_clinic.model.Facility;
import com.github.erneo97.medical_clinic.repository.FacilityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAllWithDoctors();
    }

    public Facility getFacilityById(Long id) {
        if( !facilityRepository.existsById(id) ) {
            throw new FacilityNotFound(id);
        }
        return facilityRepository.getReferenceById(id);
    }

    public Facility create(CreateFacilityCommand command) {
        Facility facility = facilityMapper.mapToEntity(command);
        return facilityRepository.save(facility);
    }
}
