package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.facility.CreateFacilityCommand;
import com.github.erneo97.medical_clinic.dto.facility.FacilityDto;
import com.github.erneo97.medical_clinic.mapper.FacilityMapper;
import com.github.erneo97.medical_clinic.service.FacilityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/facilities")
@AllArgsConstructor
public class FacilityController {
    private  final FacilityService facilityService;
    private final FacilityMapper facilityMapper;

    @GetMapping
    public List<FacilityDto> getFacilities() {
        return facilityMapper.mapToDto(facilityService.getAllFacilities());
    }

    @GetMapping("/{id}")
    public FacilityDto getFacilityById(@PathVariable Long id) {
        return  facilityMapper.mapToDto(facilityService.getFacilityById(id));
    }

    @PostMapping
    public FacilityDto createFacility(@RequestBody CreateFacilityCommand command) {
        return facilityMapper.mapToDto(facilityService.create(command));
    }
}
