package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.auth.LoginCommand;
import com.github.erneo97.medical_clinic.dto.auth.TokenDto;
import com.github.erneo97.medical_clinic.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    @Operation(summary = "Login, return token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token returned"),
            @ApiResponse(responseCode = "400", description = "No email or password"),
            @ApiResponse(responseCode = "401", description = "Wrong e-mail or password ")
    })
    public TokenDto login(@Valid @RequestBody LoginCommand loginCommand) {
        return tokenService.issue(loginCommand);
    }
}
