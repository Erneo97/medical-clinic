package com.github.erneo97.medical_clinic.model;

import lombok.AllArgsConstructor;
import com.github.erneo97.medical_clinic.dto.patient.EditPersonalDataCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class Patient {
    private final static int MAX_LENGHT_NUMBER_DATA = 20;
    private final static int MAX_LENGHT_NAME_DATA = 100;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

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
        this.user = new User(id,  email, password, Role.PATIENT);
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;

    public String getEmail() {
        return this.user.getEmail();
    }

    public void updatePatient(EditPersonalDataCommand  command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.phoneNumber = command.phoneNumber();
        this.birthday = command.birthday();
        this.user.setEmail(command.email());
    }
}
