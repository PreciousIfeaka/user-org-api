package com.precious.user_org.controllers;

import com.precious.user_org.dto.BaseResponseDto;
import com.precious.user_org.dto.organization.AddUserToOrgRequestDto;
import com.precious.user_org.dto.organization.CreateOrganizationRequestDto;
import com.precious.user_org.dto.organization.OrgPagedResponseDto;
import com.precious.user_org.dto.organization.OrganizationResponseDto;
import com.precious.user_org.services.OrganizationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
@Data
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping()
    public ResponseEntity<BaseResponseDto<OrganizationResponseDto>> createOrganization(
            @Valid @RequestBody CreateOrganizationRequestDto dto
            ) {

        BaseResponseDto<OrganizationResponseDto> response =
                new BaseResponseDto<>(
                        "success",
                        "Successfully created organization",
                        this.organizationService.createOrganization(dto)
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<OrganizationResponseDto>> getOrgById(
            @PathVariable UUID id
            ) {

        BaseResponseDto<OrganizationResponseDto> response =
                new BaseResponseDto<>(
                        "success",
                        "Successfully retrieved organizations",
                        this.organizationService.getOrganizationById(id)
                );
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<BaseResponseDto<OrgPagedResponseDto>> getAllOrg(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit
            ) {

        BaseResponseDto<OrgPagedResponseDto> response =
                new BaseResponseDto<>(
                        "success",
                        "Successfully retrieved all organizations",
                        new OrgPagedResponseDto(this.organizationService.getAllOrganizations(page, limit))
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<BaseResponseDto<Object>> addUserToOrg(
            @PathVariable UUID id,
            @Valid @RequestBody AddUserToOrgRequestDto dto
            ) throws BadRequestException {

        this.organizationService.addUserToOrganization(dto.getUserId(), id);

        BaseResponseDto<Object> response = new BaseResponseDto<>(
                "success",
                "successfully added user to organization",
                null
            );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
