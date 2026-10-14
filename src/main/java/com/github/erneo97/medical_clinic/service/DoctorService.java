package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.mapper.DoctorMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.getReferenceById(id);
    }
}
