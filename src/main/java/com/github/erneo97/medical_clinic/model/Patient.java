package com.github.erneo97.medical_clinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient {
    private final static int MAX_LENGHT_NUMBER_DATA = 20;
    private final static int MAX_LENGHT_NAME_DATA = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = MAX_LENGHT_NUMBER_DATA)
    private String idCardNo;

    @Column(nullable = false, length = MAX_LENGHT_NAME_DATA)
    private String firstName;

    @Column(nullable = false, length = MAX_LENGHT_NAME_DATA)
    private String lastName;

    @Column(length = MAX_LENGHT_NUMBER_DATA)
    private String phoneNumber;

    @Column(nullable = false)
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
