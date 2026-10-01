package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.EditPasswordCommand;
import com.github.erneo97.medical_clinic.dto.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.PatientCreateCommand;
import com.github.erneo97.medical_clinic.dto.PatientDto;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/patients")
@RequiredArgsConstructor
@Tag(name = "PatientController", description = "API for managing patients, including creating, retrieving, updating and deleting patient data.")
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    @Operation(summary = "return list of all patients")
    public List<PatientDto> findAll() {
        return patientMapper.toDto(patientService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "find patient by id")
    public PatientDto findById(@PathVariable Long id) {
        return patientMapper.toDto(patientService.findById(id));
    }

    @GetMapping(params = "email")
    @Operation(summary = "find patient by email")
    public PatientDto findByEmail(@RequestParam String email) {
        return patientMapper.toDto(patientService.findByEmail(email));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "create a patient")
    public PatientDto create(@Valid @RequestBody PatientCreateCommand command) {
        return patientMapper.toDto(patientService.create(command));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a patient data like: firstname, lastname, phone number, birth date ")
    public PatientDto update(@PathVariable Long id, @Valid @RequestBody EditPersonalDataCommand command) {
        return patientMapper.toDto( patientService.update(id, command));
    }

    @PatchMapping("{id}/password")
    @Operation(summary = "change password the patient")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody EditPasswordCommand command) {
        patientService.chanePassword(id, command);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete the patient")
    public void delete(@PathVariable Long id) {
        patientService.removeById(id);
    }
}
