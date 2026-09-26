package com.github.erneo97.medical_clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MedicalClinicApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(MedicalClinicApplication.class, args);
//		context.getBean(VisitReminderService.class).remind("anna.kowalska@example.com");
	}

}
