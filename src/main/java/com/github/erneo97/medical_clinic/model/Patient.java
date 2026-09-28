package com.github.erneo97.medical_clinic.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Patient {
    private Long id;
    private String email, password, idCardNo, firstName, lastName, phoneNumber;
    private LocalDate birthday;

    public Patient() {}

    public Patient(Long id, String email, String password, String idCardNo, String firstName, String lastName, String phoneNumber, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }
}
