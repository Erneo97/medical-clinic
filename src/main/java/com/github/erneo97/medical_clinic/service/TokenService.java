package com.github.erneo97.medical_clinic.service;

import com.github.erneo97.medical_clinic.config.SecurityConfig;
import com.github.erneo97.medical_clinic.dto.auth.LoginCommand;
import com.github.erneo97.medical_clinic.dto.auth.TokenDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class TokenService {
    private AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final Clock clock;

    private final int timeout = 3600;


    public TokenDto issue(LoginCommand command) {
        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(command.email(), command.password()));

        Instant now = Instant.now(clock);
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(SecurityConfig.ISSUER)
                .subject(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(timeout))
                .claim("roles", roles)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenDto(token, "Bearer", timeout);
    }
}
