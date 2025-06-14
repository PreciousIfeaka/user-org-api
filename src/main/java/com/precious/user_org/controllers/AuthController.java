package com.precious.user_org.controllers;

import com.precious.user_org.dto.BaseResponseDto;
import com.precious.user_org.dto.auth.AuthResponseDto;
import com.precious.user_org.dto.auth.LoginRequestDto;
import com.precious.user_org.dto.auth.RegisterRequestDto;
import com.precious.user_org.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDto<AuthResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto dto
            ) {

        BaseResponseDto<AuthResponseDto> response = new BaseResponseDto<>(
                "success",
                "Registration successful",
                authService.registerUser(dto)
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<AuthResponseDto>> register(
            @Valid @RequestBody LoginRequestDto dto
            ) {
        BaseResponseDto<AuthResponseDto> response = new BaseResponseDto<>(
                "success",
                "Login successful",
                authService.login(dto)
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
