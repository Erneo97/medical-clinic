package com.github.erneo97.medical_clinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@Getter
@Setter
public class Doctor {
    private final static int MAX_LENGHT_NAME_DATA = 100;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Specialization specialization;

    @Column(nullable = false, length = MAX_LENGHT_NAME_DATA)
    private String firstName;

    @Column(nullable = false, length = MAX_LENGHT_NAME_DATA)
    private String lastName;
}
