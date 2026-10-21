package com.github.erneo97.medical_clinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public boolean isAvaliable() {
        return patient == null;
    }

    public boolean isInPastAccordingClock(Clock clock) {
        return startsAt.isBefore(LocalDateTime.now(clock));
    }
}
