package com.github.erneo97.medical_clinic.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class PatientTest {
    @Test
    void patientAddedToHashSetBeforeSettingId_shouldStillBeFoundAfterSettingId() {
        // given
        Patient patient = new Patient();
        Set<Patient> patients = new HashSet<>();

        // when
        patients.add(patient);
        patient.setId(42L);

        // then
        assertThat(patients).contains(patient);
    }

    @Test
    void patientsWithSameId_shouldBeEqual() {
        // given
        Patient first = new Patient();
        first.setId(42L);

        Patient second = new Patient();
        second.setId(42L);

        // then
        assertThat(first).isEqualTo(second);
    }

    @Test
    void patientsWithoutId_shouldBeNotEqual() {
        // given
        Patient first = new Patient("Jan");
        Patient second = new Patient("Jan");


        // then
        assertThat(first).isNotEqualTo(second);
    }
}