package com.github.erneo97.medical_clinic.notification;

public interface ReminderSender {
    void send(String to, String message);
}
