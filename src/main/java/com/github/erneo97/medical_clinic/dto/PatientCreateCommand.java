package com.github.erneo97.medical_clinic.dto;

import com.github.erneo97.medical_clinic.validate.ValidFieldRulers;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PatientCreateCommand (
        @NotBlank(message = ValidFieldRulers.NOT_BLANK_MESSAGE)
        @Email(message = ValidFieldRulers.FIELD_IS_NOT_EMAIL)
        String email,

        @NotBlank(message = ValidFieldRulers.NOT_BLANK_MESSAGE)
        @Size(min = ValidFieldRulers.PASSWORD_MIN_LENGTH, message = ValidFieldRulers.PASSWORD_LENGTH_MESSAGE)
        @Pattern(regexp = ValidFieldRulers.PASSWORD_REGEX, message = ValidFieldRulers.PASSWORD_PATTERN_MESSAGE)
        String password,

        @NotBlank(message = ValidFieldRulers.NOT_BLANK_MESSAGE)
        @Pattern(regexp = ValidFieldRulers.ID_CARD_NO_REGEX, message = ValidFieldRulers.ID_CARD_NO_PATTERN_MESSAGE)
        String idCardNo,

        @NotBlank(message = ValidFieldRulers.NOT_BLANK_MESSAGE)
        @Pattern(regexp = ValidFieldRulers.NAME_REGEX, message = ValidFieldRulers.FIRST_NAME_PATTERN_MESSAGE)
        String firstName,

        @NotBlank(message = ValidFieldRulers.NOT_BLANK_MESSAGE)
        @Pattern(regexp = ValidFieldRulers.NAME_REGEX, message = ValidFieldRulers.LAST_NAME_PATTERN_MESSAGE)
        String lastName,

        @NotBlank(message = ValidFieldRulers.NOT_BLANK_MESSAGE)
        @Pattern(regexp = ValidFieldRulers.PHONE_NUMBER_REGEX, message = ValidFieldRulers.PHONE_NUMBER_PATTERN_MESSAGE)
        String phoneNumber,

        @Past(message = ValidFieldRulers.AGE_PAST_MESSAGE)
        @NotNull(message = ValidFieldRulers.FILED_IS_REQUIRED_MESSAGE)
        LocalDate birthday
){
}
