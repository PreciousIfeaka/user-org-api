package com.precious.user_org.controllers;

import com.precious.user_org.dto.BaseResponseDto;
import com.precious.user_org.dto.user.UserPagedResponseDto;
import com.precious.user_org.dto.user.UserResponseDto;
import com.precious.user_org.models.User;
import com.precious.user_org.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<UserResponseDto>> getUser(
            @PathVariable UUID id
            ) {
        User user = this.userService.getUser(id);
        BaseResponseDto<UserResponseDto> response = new BaseResponseDto<>(
                "success",
                "Successfully retrieved user",
                UserResponseDto.builder()
                        .userId(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .build()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<BaseResponseDto<UserPagedResponseDto>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        Page<User> users = this.userService.getAllUsers(page, limit);

        BaseResponseDto<UserPagedResponseDto> response = new BaseResponseDto<>(
                "success",
                "Successfully retrieved all users",
                new UserPagedResponseDto(users.map(UserResponseDto::fromEntity))
        );

        return ResponseEntity.ok(response);
    }
}
