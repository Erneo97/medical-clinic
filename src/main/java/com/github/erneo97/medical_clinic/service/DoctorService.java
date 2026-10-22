package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.doctor.DoctorCreateCommand;
import com.github.erneo97.medical_clinic.exeption.DoctorNotFoundException;
import com.github.erneo97.medical_clinic.exeption.FacilityNotFoundException;
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
        return doctorRepository.findAllWithUserAndFacilities();
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
        checDoctorOrFacilityExistOrThrowException(id, facilityId);
        Facility facility = facilityRepository.getReferenceById(facilityId);
        Doctor doctor = doctorRepository.getReferenceById(id);
        doctor.addFacility(facility);
        return doctorRepository.save(doctor);
    }

    public void removeFacilityToDoctor(Long id, Long facilityId) {
        checDoctorOrFacilityExistOrThrowException(id, facilityId);
        Facility facility = facilityRepository.getReferenceById(facilityId);
        Doctor doctor = doctorRepository.getReferenceById(id);
        doctor.removeFacility(facility);
        doctorRepository.save(doctor);
    }

    public Doctor findDoctorByIdOrThrowException(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
    }

    public void checkDoctorExistOrThrowException(Long doctorId) {
        if( !doctorRepository.existsById(doctorId)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean existsById(Long doctorId) {
        return doctorRepository.existsById(doctorId);
    }

    private void checDoctorOrFacilityExistOrThrowException(Long doctorId, Long facilityId) {
        checkDoctorExistOrThrowException(doctorId);
        if( !facilityRepository.existsById(facilityId)) {
            throw new FacilityNotFoundException(facilityId);
        }
    }
}
