package com.github.erneo97.medical_clinic.facade;

import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.service.DoctorService;
import com.github.erneo97.medical_clinic.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserFacade {
    private final DoctorService doctorService;
    private final PatientService patientService;

    public boolean doctorExistsById(Long id){
        return doctorService.existsById(id);
    }
    public void checkDoctorExistOrThrowException(Long doctorId) {
        doctorService.checkDoctorExistOrThrowException(doctorId);
    }

    public Doctor findDoctorById(Long id){
        return doctorService.findDoctorByIdOrThrowException(id);
    }

    public Patient findPatientById(Long id){
        return patientService.findById(id);
    }
}
