package com.github.erneo97.medical_clinic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/postman")
@Slf4j
public class PostmanController {

    @GetMapping
    public String postman() throws IOException {
        log.info("take postman file");
        ClassPathResource resource = new ClassPathResource("medical-clinic.postman_collection.json");
        try (InputStream in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
