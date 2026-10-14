package com.github.erneo97.medical_clinic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class Patient {
    private Long id;
    private String email, password, idCardNo, firstName, lastName, phoneNumber;
    private LocalDate birthday;

    public void updatePassword(String password) {
        this.password = password;
    }
}
