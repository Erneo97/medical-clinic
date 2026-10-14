package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.doctor.DoctorCreateCommand;
import com.github.erneo97.medical_clinic.dto.doktor.DoctorDto;
import com.github.erneo97.medical_clinic.mapper.DoctorMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.DoctorRepository;
import com.github.erneo97.medical_clinic.repository.PatientRepository;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.getReferenceById(id);
    }

    public Doctor create(DoctorCreateCommand command) {
        User user = userRepository.getReferenceById(command.userId());

        Doctor newDoctor = doctorMapper.toDoctor(command);
        newDoctor.setUser(user);

        return doctorRepository.save(newDoctor);
    }
}
