package com.github.erneo97.medical_clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class MedicalClinicApplication {

	public static void main(String[] args) {
//		SpringApplication.run(MedicalClinicApplication.class, args);
		var context = SpringApplication.run(MedicalClinicApplication.class, args);
		Arrays.stream(context.getBeanDefinitionNames()).sorted()
				.forEach(System.out::println);

	}

}
