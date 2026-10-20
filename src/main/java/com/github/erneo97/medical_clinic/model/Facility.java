package com.github.erneo97.medical_clinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facilities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String city;

    private String zipCode;

    private String street;

    private int number;

    @ManyToMany(mappedBy = "facilities", fetch =  FetchType.LAZY)
    @Builder.Default
    private Set<Doctor> doctors = new HashSet<>();

}
