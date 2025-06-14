package com.precious.user_org.services;

import com.precious.user_org.dto.organization.CreateOrganizationRequestDto;
import com.precious.user_org.dto.organization.OrganizationResponseDto;
import com.precious.user_org.dto.user.UserResponseDto;
import com.precious.user_org.exceptions.BadRequestException;
import com.precious.user_org.exceptions.ForbiddenException;
import com.precious.user_org.exceptions.ResourceNotFoundException;
import com.precious.user_org.models.Organization;
import com.precious.user_org.models.User;
import com.precious.user_org.repositories.OrganizationRepository;
import com.precious.user_org.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Data
@AllArgsConstructor
@Transactional
@Validated
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public OrganizationResponseDto createOrganization(CreateOrganizationRequestDto dto) {
        User authenticatedUser = this.userService.getAuthenticatedUser();

        Organization newOrg = Organization.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdBy(authenticatedUser)
                .users(new HashSet<>(Set.of(authenticatedUser)))
                .build();


        Organization savedOrg = organizationRepository.save(newOrg);

        authenticatedUser.getOrganizations().add(savedOrg);

        this.userRepository.save(authenticatedUser);

        return OrganizationResponseDto.builder()
                .orgId(savedOrg.getId())
                .name(savedOrg.getName())
                .description(savedOrg.getDescription())
                .build();
    }

    public OrganizationResponseDto getOrganizationById(UUID id) {
        User authenticatedUser = this.userService.getAuthenticatedUser();


        Organization org = this.organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        if (!this.organizationRepository.findByUserMember(authenticatedUser.getId()).contains(org)) {
            throw new ForbiddenException("Not a member of this organization");
        }

        return OrganizationResponseDto.builder()
                .orgId(org.getId())
                .name(org.getName())
                .description(org.getDescription())
                .build();
    }

    public Page<OrganizationResponseDto> getAllOrganizations(int page, int size) {
        User authenticatedUser = this.userService.getAuthenticatedUser();

        Page<Organization> orgs = this.organizationRepository.findByUserMember(
                authenticatedUser.getId(),
                PageRequest.of(page, size)
        );

        return orgs.map(OrganizationResponseDto::fromEntity);
    }

    public void addUserToOrganization(UUID userId, UUID orgId) throws BadRequestException {
        User authenticatedUser = this.userService.getAuthenticatedUser();

        Organization org = this.organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        List<Organization> myOrgs = this.organizationRepository.findByCreatedBy_Id(authenticatedUser.getId());

        if (!myOrgs.contains(org)) {
            throw new ForbiddenException("Unauthorized access to add user to this org");
        }
        else if (
                org.getUsers().stream().map(UserResponseDto::fromEntity)
                        .distinct().toList()
                        .contains(this.userService.getUser(userId))
        ) {
            throw new BadRequestException("User is already a member of this organization");
        }

        User user = this.userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        org.getUsers().add(user);
        user.getOrganizations().add(org);

        this.userRepository.save(user);
    }
}
