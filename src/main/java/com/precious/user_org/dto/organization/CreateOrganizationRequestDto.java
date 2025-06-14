package com.precious.user_org.dto.organization;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequestDto {
    @NotBlank(message = "name is required")
    private String name;

    private String description;
}
