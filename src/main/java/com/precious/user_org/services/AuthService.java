package com.precious.user_org.services;

import com.precious.user_org.config.JwtService;
import com.precious.user_org.dto.auth.LoginRequestDto;
import com.precious.user_org.dto.auth.RegisterRequestDto;
import com.precious.user_org.dto.auth.AuthResponseDto;
import com.precious.user_org.dto.user.UserResponseDto;
import com.precious.user_org.exceptions.BadRequestException;
import com.precious.user_org.exceptions.ResourceNotFoundException;
import com.precious.user_org.models.User;
import com.precious.user_org.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto registerUser(RegisterRequestDto dto) {
        UserResponseDto createdUser = this.userService.createUser(dto);
        User user = this.userRepository.findById(createdUser.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        String accessToken = this.jwtService.generateJwt(user);

        UserResponseDto userResponse = UserResponseDto.fromEntity(user);

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

        User user = this.userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        String authToken = this.jwtService.generateJwt(user);

        UserResponseDto userResponse = UserResponseDto.fromEntity(user);

        return new AuthResponseDto(userResponse, authToken);
    }
}
