package com.github.erneo97.medical_clinic.config;

import com.github.erneo97.medical_clinic.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@Import(JwtConfig.class)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter converter) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/doctor").hasRole(Role.ADMIN.getValue())
                        .requestMatchers(HttpMethod.POST, "/facilities").hasRole(Role.ADMIN.getValue())
                        .requestMatchers(HttpMethod.GET, "/doctors").hasRole(Role.ADMIN.getValue())
                        .requestMatchers(HttpMethod.GET, "/facilities/**").authenticated()
                        .requestMatchers("/doctors/*/facilities/*").hasRole(Role.DOCTOR.getValue())
                        .requestMatchers(HttpMethod.POST, "/visits").hasRole(Role.ADMIN.getValue())
                        .requestMatchers("/doctors/**").hasRole(Role.DOCTOR.getValue())

                        .requestMatchers(HttpMethod.POST, "/patients").anonymous()
                        .requestMatchers(HttpMethod.GET, "/patients").hasRole(Role.ADMIN.getValue())
                        .requestMatchers( "/users/**").hasRole(Role.ADMIN.getValue())

                        .requestMatchers("/patients/**","/doctors/**", "/visits/**").hasRole(Role.PATIENT.getValue())
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").hasRole(Role.ADMIN.getValue())
                        .requestMatchers("/postman/**").hasRole(Role.ADMIN.getValue())
                        .requestMatchers(HttpMethod.POST, "/auth/token").permitAll()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt( jwt -> jwt.jwtAuthenticationConverter(converter)));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
