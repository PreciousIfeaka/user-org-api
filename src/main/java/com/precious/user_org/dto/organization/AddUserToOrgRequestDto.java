package com.precious.user_org.dto.organization;

import lombok.Data;

import java.util.UUID;

@Data
public class AddUserToOrgRequestDto {
    private UUID userId;
}
