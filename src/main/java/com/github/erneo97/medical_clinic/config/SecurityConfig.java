package com.github.erneo97.medical_clinic.config;

import com.github.erneo97.medical_clinic.model.Role;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;


@Configuration
public class SecurityConfig {
    public static final String ISSUER = "mediacl-clinic";

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public SecurityConfig() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        RSAKey jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(ISSUER));
        return decoder;
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
