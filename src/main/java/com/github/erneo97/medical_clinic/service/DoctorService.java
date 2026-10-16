package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.doctor.DoctorCreateCommand;
import com.github.erneo97.medical_clinic.exeption.FacilityNotFound;
import com.github.erneo97.medical_clinic.mapper.DoctorMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Facility;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.DoctorRepository;
import com.github.erneo97.medical_clinic.repository.FacilityRepository;
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
    private final FacilityRepository facilityRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.getReferenceById(id);
    }

    public Doctor create(DoctorCreateCommand command) {
        User user = userRepository.getReferenceById(command.userId());

        Doctor newDoctor =  doctorMapper.toDoctor(command);
        newDoctor.setUser(user);

        return doctorRepository.save(newDoctor);
    }

    public void deleteDoctorById(Long id) {
        doctorRepository.deleteById(id);
    }

    public Doctor addFacilityToDoctor(Long id, Long facilityId) {
        checDoctorOrFacilityExistOrThrowExeption(id, facilityId);
        Facility facility = facilityRepository.getReferenceById(facilityId);
        Doctor doctor = doctorRepository.getReferenceById(id);
        doctor.addFacility(facility);
        return doctorRepository.save(doctor);
    }

    public void removeFacilityToDoctor(Long id, Long facilityId) {
        checDoctorOrFacilityExistOrThrowExeption(id, facilityId);
        Facility facility = facilityRepository.getReferenceById(facilityId);
        Doctor doctor = doctorRepository.getReferenceById(id);
        doctor.removeFacility(facility);
        doctorRepository.save(doctor);
    }

    private void checDoctorOrFacilityExistOrThrowExeption(Long doctorId, Long facilityId) {
        if( !doctorRepository.existsById(doctorId)) {
            throw new IllegalArgumentException();
        }
        if( !facilityRepository.existsById(facilityId)) {
            throw new FacilityNotFound(facilityId);
        }
    }
}
