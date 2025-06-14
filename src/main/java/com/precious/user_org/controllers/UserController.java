package com.precious.user_org.controllers;

import com.precious.user_org.dto.BaseResponseDto;
import com.precious.user_org.dto.user.UserPagedResponseDto;
import com.precious.user_org.dto.user.UserResponseDto;
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
        UserResponseDto user = this.userService.getUser(id);
        BaseResponseDto<UserResponseDto> response = new BaseResponseDto<>(
                "success",
                "Successfully retrieved user",
                user
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<BaseResponseDto<UserPagedResponseDto>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        Page<UserResponseDto> users = this.userService.getAllUsers(page, limit);

        BaseResponseDto<UserPagedResponseDto> response = new BaseResponseDto<>(
                "success",
                "Successfully retrieved all users",
                new UserPagedResponseDto(users)
        );

        return ResponseEntity.ok(response);
    }
}
