package com.precious.user_org.dto.organization;

import com.precious.user_org.models.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponseDto {
    private UUID orgId;
    private String name;
    private String description;

    public static OrganizationResponseDto fromEntity(Organization org) {
        return OrganizationResponseDto.builder()
                .orgId(org.getId())
                .name(org.getName())
                .description(org.getDescription())
                .build();
    }
}
