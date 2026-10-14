package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.exeption.DoctorNotFound;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Visit;
import com.github.erneo97.medical_clinic.repository.DoctorRepository;
import com.github.erneo97.medical_clinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Service
@AllArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;

    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    public Visit create(@RequestBody VisitCreateCommand command) {
        if(!doctorRepository.existsById(command.doctorId())) {
            throw new DoctorNotFound("Doctor Not Found with ID: " + command.doctorId());
        }
        Doctor doctor = doctorRepository.findById(command.doctorId())
                .orElseThrow(() -> new DoctorNotFound("Doctor Not Found with ID: " + command.doctorId()));
        Visit visit = visitMapper.toVisit(command);
        visit.setDoctor(doctor);
        return visitRepository.save(visit);
    }
}
