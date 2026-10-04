package com.github.erneo97.medical_clinic.dto;

import com.github.erneo97.medical_clinic.validate.ValidFieldRulers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EditPasswordCommand(
        @NotBlank(message = ValidFieldRulers.NOT_BLANK_MESSAGE)
        @Size(min = ValidFieldRulers.PASSWORD_MIN_LENGTH, message = ValidFieldRulers.PASSWORD_LENGTH_MESSAGE)
        @Pattern(regexp = ValidFieldRulers.PASSWORD_REGEX, message = ValidFieldRulers.PASSWORD_PATTERN_MESSAGE)
        String password
) {
}
