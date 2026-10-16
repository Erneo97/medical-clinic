package com.github.erneo97.medical_clinic.controller;

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

    @GetMapping
    public List<Facility> getFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping("/{id}")
    public Facility getFacilityById(@PathVariable Long id) {
        return facilityService.getFacilityById(id);
    }
}
