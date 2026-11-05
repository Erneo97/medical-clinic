package com.github.erneo97.medical_clinic.mapper;

import com.github.erneo97.medical_clinic.dto.doktor.UserCreateCommand;
import com.github.erneo97.medical_clinic.dto.doktor.UserDto;
import com.github.erneo97.medical_clinic.dto.patient.PatientCreateCommand;
import com.github.erneo97.medical_clinic.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserCreateCommand command);

    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUser(PatientCreateCommand command);
}
