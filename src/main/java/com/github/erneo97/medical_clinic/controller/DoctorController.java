package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.doktor.DoctorDto;
import com.github.erneo97.medical_clinic.mapper.DoctorMapper;
import com.github.erneo97.medical_clinic.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/doctors")
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorMapper.toDto(doctorService.getAllDoctors());
    }

    @GetMapping("{id}")
    public DoctorDto findById(@PathVariable Long id) {
        return doctorMapper.toDto(doctorService.getDoctorById(id));
    }


}
