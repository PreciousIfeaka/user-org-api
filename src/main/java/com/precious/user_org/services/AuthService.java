package com.precious.user_org.services;

import com.precious.user_org.config.JwtService;
import com.precious.user_org.dto.auth.LoginRequestDto;
import com.precious.user_org.dto.auth.RegisterRequestDto;
import com.precious.user_org.dto.auth.AuthResponseDto;
import com.precious.user_org.dto.user.UserResponseDto;
import com.precious.user_org.exceptions.BadRequestException;
import com.precious.user_org.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto registerUser(RegisterRequestDto dto) {
        User createdUser = this.userService.createUser(dto);


        String accessToken = this.jwtService.generateJwt(createdUser);

        UserResponseDto userResponse = new UserResponseDto(
                createdUser.getId(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getEmail(),
                createdUser.getPhone()
        );

        return new AuthResponseDto(userResponse, accessToken);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail().toLowerCase(),
                            dto.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid email or password");
        }

        User user = this.userService.getUser(dto.getEmail().toLowerCase());
        String authToken = this.jwtService.generateJwt(user);

        UserResponseDto userResponse = new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone()
        );

        return new AuthResponseDto(userResponse, authToken);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return this.userService.getUser(username);
        }
        return null;
    }
}
