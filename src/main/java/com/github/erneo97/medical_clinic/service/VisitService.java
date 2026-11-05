package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.visit.VisitCreateCommand;
import com.github.erneo97.medical_clinic.exeption.*;
import com.github.erneo97.medical_clinic.mapper.VisitMapper;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.model.Visit;
import com.github.erneo97.medical_clinic.repository.DoctorRepository;
import com.github.erneo97.medical_clinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class VisitService {
    private static final Duration QUARTER = Duration.ofMinutes(15);
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final Clock clock;

    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    @Transactional
    @ResponseStatus(value = HttpStatus.CREATED)
    public Visit create(@RequestBody VisitCreateCommand command) {
        if (!doctorService.existsById(command.doctorId())) {
            throw new DoctorNotFoundException("Doctor Not Found with ID: " + command.doctorId());
        }
        validateSlot(command.startsAt(), command.endsAt());
        checkOverlap(command.doctorId(), command.startsAt(), command.endsAt());

        Doctor doctor = doctorService.findDoctorByIdOrThrowException(command.doctorId());
        Visit visit = visitMapper.toVisit(command);
        visit.setDoctor(doctor);
        return visitRepository.save(visit);
    }

    @Transactional(readOnly = true)
    public List<Visit> findAllByDoctorId(Long doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }

    @Transactional
    public Visit bookVisit(Long id, Long patientId) {
        Visit visit = this.findVisitByIdOrException(id);
        Patient patient = patientService.findById(patientId);

        if (!visit.isAvaliable()) {
            throw new VisitAlreadyBookedException("This visit is booked");
        }
        if (visit.isInPastAccordingClock(clock)) {
            throw new VisitInPastException("The visit has already taken place");
        }
        visit.setPatient(patient);
        return visit;
    }

    @Transactional(readOnly = true)
    public Visit findVisitByIdOrException(Long id) {
        return visitRepository.findById(id).orElseThrow(() -> new VisitNotFoundException(String.format("Visit with id %d not exist", id)));
    }

    @Transactional(readOnly = true)
    public List<Visit> getPatientVisit(Long patientId) {
        return visitRepository.findByPatientId(patientId);
    }

    @Transactional
    public List<Visit> substitute(Long doctorId, Long substituteId) {
        doctorService.checkDoctorExistOrThrowException(doctorId);
        Doctor doctorSubstitute = doctorService.findDoctorByIdOrThrowException(substituteId);
        List<Visit> visits = getSortedVisitsByDoctorId(doctorId);
        substituteVisitsToDoctor(visits, doctorSubstitute);
        return visits;
    }

    private List<Visit> getSortedVisitsByDoctorId(Long doctorId) {
        return visitRepository.findByDoctorId(doctorId)
                .stream()
                .sorted(Comparator.comparing(Visit::getStartsAt))
                .toList();
    }

    private void substituteVisitsToDoctor(List<Visit> visits, Doctor doctor) {
        for(Visit visit : visits) {
            checkOverlap(doctor.getId(), visit.getStartsAt(), visit.getEndsAt());
            visit.setDoctor(doctor);
            visitRepository.save(visit);
        }
    }

    private void validateSlot(LocalDateTime slotStart, LocalDateTime slotEnd) {
        if (slotStart.isBefore(LocalDateTime.now(clock)) || slotEnd.isBefore(LocalDateTime.now(clock))) {
            throw new InvalidVisitException("Slot Start Date or Slot End Dates are in past");
        }
        if (slotStart.getMinute() % 15 != 0 || slotStart.getSecond() != 0 || slotStart.getNano() != 0) {
            throw new InvalidVisitException("a session must start on a full quarter-hour");
        }
        Duration length = Duration.between(slotStart, slotEnd);
        if (!length.isPositive() || length.toNanos() % QUARTER.toNanos() != 0) {
            throw new InvalidVisitException(
                    "session length must be a positive multiple of 15 minutes");
        }
    }

    private void checkOverlap(Long doctorId, LocalDateTime start, LocalDateTime end) {
        if (visitRepository.existsByDoctorIdAndStartsAtBeforeAndEndsAtAfter(
                doctorId, end, start)) {
            throw new VisitOverlapsException(String.format("Visit already exists for Doctor on that time %s - %s", start.toString(), end.toString()));
        }
    }
}
