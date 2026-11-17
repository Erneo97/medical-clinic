package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.doktor.UserCreateCommand;
import com.github.erneo97.medical_clinic.dto.doktor.UserDto;
import com.github.erneo97.medical_clinic.mapper.UserMapper;
import com.github.erneo97.medical_clinic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserCreateCommand command) {
        return userMapper.toUserDto(userService.createUser(command));
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        return  userMapper.toUserDto(userService.findById(id));
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userMapper.toUserDto(userService.findAllUsers());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.removeUser(id);
    }
}
