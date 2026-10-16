package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.FacilityDto;
import com.github.erneo97.medical_clinic.mapper.FacilityMapper;
import com.github.erneo97.medical_clinic.model.Facility;
import com.github.erneo97.medical_clinic.service.FacilityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
