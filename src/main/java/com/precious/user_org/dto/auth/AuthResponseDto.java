package com.precious.user_org.dto.auth;


import com.precious.user_org.dto.user.UserResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private UserResponseDto user;
    @NotBlank private String accessToken;
}
