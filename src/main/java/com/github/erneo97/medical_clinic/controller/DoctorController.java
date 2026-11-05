package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.doctor.DoctorCreateCommand;
import com.github.erneo97.medical_clinic.dto.doctor.DoctorDto;
import com.github.erneo97.medical_clinic.dto.doctor.SubstituteDoctorCommand;
import com.github.erneo97.medical_clinic.dto.visit.VisitDto;
import com.github.erneo97.medical_clinic.mapper.DoctorMapper;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.service.DoctorService;
import com.github.erneo97.medical_clinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/doctors")
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final VisitService visitService;
    private final VisitMapper visitMapper;
    private final DoctorMapper doctorMapper;

    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorMapper.toDto(doctorService.getAllDoctors());
    }

    @GetMapping("{id}")
    public DoctorDto findById(@PathVariable Long id) {
        return doctorMapper.toDto(doctorService.getDoctorById(id));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public DoctorDto saveDoctor(@RequestBody DoctorCreateCommand command) {
        return doctorMapper.toDto(doctorService.create(command));
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
    }

    @PostMapping("/{id}/facilities/{facilityId}")
    public DoctorDto addFacility(@PathVariable Long id, @PathVariable Long facilityId) {
        return doctorMapper.toDto(doctorService.addFacilityToDoctor(id, facilityId));
    }

    @PostMapping("/{id}/substitutions")
    public List<VisitDto> substituteDoctor(@PathVariable Long id, @RequestBody SubstituteDoctorCommand command) {
        return visitMapper.toVisit(visitService.substitute(id, command.substituteId()));
    }


    @DeleteMapping("/{id}/facilities/{facilityId}")
    public void removeFacility(@PathVariable Long id, @PathVariable Long facilityId) {
        doctorService.removeFacilityToDoctor(id, facilityId);
    }
}
