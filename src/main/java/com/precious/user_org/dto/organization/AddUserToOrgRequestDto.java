package com.precious.user_org.dto.organization;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddUserToOrgRequestDto {
    private UUID userId;
}
