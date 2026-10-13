package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.doktor.UserCreateCommand;
import com.github.erneo97.medical_clinic.dto.doktor.UserDto;
import com.github.erneo97.medical_clinic.exeption.PatientNotFound;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    public User createUser(UserCreateCommand command) {
        return userRepository.save(patientMapper.toUser(command));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new PatientNotFound(id));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
