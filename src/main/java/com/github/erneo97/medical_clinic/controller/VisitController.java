package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.dto.visit.VisitDto;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping
    public List<VisitDto> findAll() {
        return visitMapper.toVisit(visitService.findAll());
    }

    @GetMapping(params = "doctorId")
    public List<VisitDto> findByDoctorId(@RequestParam Long doctorId) {
        return  visitMapper.toVisit(visitService.findAllByDoctorId(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    public List<VisitDto> getPatientVisits(@PathVariable Long patientId) {
        return visitMapper.toVisit(visitService.getPatientVisit(patientId));
    }

    @PostMapping
    public VisitDto create(@RequestBody VisitCreateCommand command) {
        return visitMapper.toVisit(visitService.create(command));
    }

    @PatchMapping("/{id}/book/{patientId}")
    public VisitDto book(@PathVariable Long id, @PathVariable Long patientId) {
        return visitMapper.toVisit(visitService.bookVisit(id, patientId));
    }
}
