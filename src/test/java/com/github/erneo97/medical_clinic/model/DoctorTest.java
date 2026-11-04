package com.github.erneo97.medical_clinic.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    @Test
    void addFacilityNextRemoveFacility_facilityListEmpty() {
        // given
        Doctor doctor = new Doctor();
        Facility facility = new Facility();

        // when
        doctor.addFacility(facility);
        doctor.removeFacility(facility);
        facility.setId(1L);

        // then
        assertTrue(doctor.getFacilities().isEmpty());

    }
}