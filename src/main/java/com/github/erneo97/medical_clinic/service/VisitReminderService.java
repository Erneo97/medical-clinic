package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.notification.ReminderSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VisitReminderService {
    private final ReminderSender reminderSender;

    public void remind(String to) {
        reminderSender.send(to, "przypomnienie o wizycie jutro o 10:00");
    }
}
