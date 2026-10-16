package com.github.erneo97.medical_clinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    private final static int MAX_LENGTH_NAME_DATA = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = MAX_LENGTH_NAME_DATA)
    private String firstName;

    @Column(nullable = false, length = MAX_LENGTH_NAME_DATA)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @ManyToMany
    @JoinTable(
            name = "doctor_facility",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id"))
    @Builder.Default
    private Set<Facility> facilities = new HashSet<>();

    public void addFacility(Facility facility) {
        facilities.add(facility);
        facility.getDoctors().add(this);
    }

    public void removeFacility(Facility facility) {
        facilities.remove(facility);
        facility.getDoctors().remove(this);
    }
}
