package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.doktor.UserCreateCommand;
import com.github.erneo97.medical_clinic.exeption.UserNotFoundException;
import com.github.erneo97.medical_clinic.mapper.UserMapper;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(UserCreateCommand command) {
        return userRepository.save(userMapper.toUser(command));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void removeUser(Long id) {
        findNotPatientOrThrow(id);
        userRepository.deleteById(id);
    }

    private void findNotPatientOrThrow(Long id) {
        this.findById(id);
    }
}
