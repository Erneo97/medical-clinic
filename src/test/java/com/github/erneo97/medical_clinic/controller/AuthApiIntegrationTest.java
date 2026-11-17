package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.model.Role;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
public class AuthApiIntegrationTest {
    static final String ADMIN_EMAIL = "admin@example.com";
    static final String ADMIN_PASSWORD = "password";
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository  userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void adminGetsTokenValidForOneHour() throws Exception {
        // given
        seedAdmin();
        assertThat(userRepository.findByEmail(ADMIN_EMAIL)).isPresent();
        String body = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", ADMIN_EMAIL, ADMIN_PASSWORD);

        // when
        ResultActions result = mockMvc.perform(post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void wrongPasswordReturns401() throws Exception {
        // given
        seedAdmin();
        String body = String.format("{\"email\": \"%s\",\"password\":\"zle\"}", ADMIN_EMAIL);

        // when
        ResultActions result = mockMvc.perform(post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    void basicAuthOnPatientsReturns401() throws Exception {
        // given
        seedAdmin();

        // when
        ResultActions result = mockMvc.perform(get("/patients")
                .with(httpBasic(ADMIN_EMAIL, ADMIN_PASSWORD)));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    void invalidBearerReturns401() throws Exception {
        // given
        seedAdmin();

        // when
        ResultActions result = mockMvc.perform(get("/patients")
                .header("Authorization", "Bearer abc"));

        // then
        result.andExpect(status().isUnauthorized());
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
            userRepository.saveAndFlush(User.builder()
                    .email(ADMIN_EMAIL)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(Role.ADMIN)
                    .build());
        }
    }
}
