package com.github.erneo97.medical_clinic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;

@ToString
public enum Role {
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR"),
    NOTHING("NOTHING");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @JsonValue
    public String getValue() {
        return name;
    }

    @JsonCreator
    public static Role fromValue(String value) {
        for (Role role : values()) {
            if (role.name.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknow role: " + value);
    }
}

