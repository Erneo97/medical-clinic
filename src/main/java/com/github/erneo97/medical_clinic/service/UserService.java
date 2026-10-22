package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.dto.doktor.UserCreateCommand;
import com.github.erneo97.medical_clinic.exeption.UserNotFoundException;
import com.github.erneo97.medical_clinic.mapper.UserMapper;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User createUser(UserCreateCommand command) {
        return userRepository.save(userMapper.toUser(command));
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void removeUser(Long id) {
        findNotPatientOrThrow(id);
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    private void findNotPatientOrThrow(Long id) {
        this.findById(id);
    }
}
