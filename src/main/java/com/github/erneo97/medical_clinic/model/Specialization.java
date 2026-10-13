package com.github.erneo97.medical_clinic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Specialization {
    DERMATOLOGIST("DERMATOLOGIST"),
    CARDIOLOGIST("CARDIOLOGIST"),
    PEDIATRICIAN("PEDIATRICIAN"),
    UNKNOWN("UNKNOWN");

    private final String specjalization;

    Specialization(String specjalization) {
        this.specjalization = specjalization;
    }

    @JsonValue
    public String getValue() {
        return specjalization;
    }

    @JsonCreator
    public static Specialization fromValue(String value) {
        for (Specialization role : values()) {
            if (role.specjalization.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknow specialization: " + value);
    }
}
