package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.EditPasswordCommand;
import com.github.erneo97.medical_clinic.dto.EditPersonalDataCommand;
import com.github.erneo97.medical_clinic.dto.PatientCreateCommand;
import com.github.erneo97.medical_clinic.dto.PatientDto;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    public List<PatientDto> findAll() {
        return patientMapper.toDto(patientService.findAll());
    }

    @GetMapping("/{id}")
    public PatientDto findById(@PathVariable Long id) {
        return patientMapper.toDto(patientService.findById(id));
    }

    @GetMapping(params = "email")
    public PatientDto findByEmail(@RequestParam String email) {
        return patientMapper.toDto(patientService.findByEmail(email));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PatientDto create(@Valid @RequestBody PatientCreateCommand command) {
        return patientMapper.toDto(patientService.create(command));
    }

    @PutMapping("/{id}")
    public PatientDto update(@PathVariable Long id, @Valid @RequestBody EditPersonalDataCommand command) {
        return patientMapper.toDto( patientService.update(id, command));
    }

    @PatchMapping("{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody EditPasswordCommand command) {
        patientService.chanePassword(id, command);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = patientService.removeById(id);
        System.err.println("delete : " + deleted);
        return deleted ?
                ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
