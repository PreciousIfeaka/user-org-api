package com.precious.user_org.services;

import com.precious.user_org.dto.auth.RegisterRequestDto;
import com.precious.user_org.dto.user.UserResponseDto;
import com.precious.user_org.exceptions.ResourceNotFoundException;
import com.precious.user_org.exceptions.UnauthorizedException;
import com.precious.user_org.models.Organization;
import com.precious.user_org.models.Role;
import com.precious.user_org.models.User;
import com.precious.user_org.repositories.OrganizationRepository;
import com.precious.user_org.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto createUser(RegisterRequestDto registerRequestDto) {
        User newUser = new User();

        newUser.setFirstName(registerRequestDto.getFirstName());
        newUser.setLastName(registerRequestDto.getLastName());
        newUser.setEmail(registerRequestDto.getEmail().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        newUser.setRole(Role.USER);
        newUser.setPhone(registerRequestDto.getPhone());
        newUser.setOrganizations(new HashSet<>());

        User savedUser = userRepository.save(newUser);

        Organization organization = new Organization();
        organization.setName(registerRequestDto.getFirstName() + "'s Organization");
        organization.setCreatedBy(savedUser);

        Organization savedOrganization = organizationRepository.save(organization);

        savedUser.getOrganizations().add(savedOrganization);
        savedOrganization.getUsers().add(savedUser);

        return UserResponseDto.fromEntity(this.userRepository.save(savedUser));
    }

    private boolean isUserInOrgs(List<Organization> orgs, UUID userId) {
        return orgs.stream()
                .anyMatch(org -> org.getUsers() != null &&
                        org.getUsers().stream()
                                .anyMatch(user -> userId.equals(user.getId())));
    }

    public UserResponseDto getUser(Object identifier) {
        User authUser = this.getAuthenticatedUser();
        List<Organization> myOrgs = this.organizationRepository.findByCreatedBy_Id(authUser.getId());

        User user;
        if (identifier instanceof String email && email.contains("@")) {
            user = this.userRepository.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (!isUserInOrgs(myOrgs, user.getId())) {
                throw new UnauthorizedException("Unauthorized");
            }
            return UserResponseDto.fromEntity(user);
        } else if (identifier instanceof UUID id) {
            user = this.userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!isUserInOrgs(myOrgs, user.getId())) {
                throw new UnauthorizedException("Unauthorized");
            }
            return UserResponseDto.fromEntity(user);
        }
        throw new IllegalArgumentException("Unsupported identifier type: " + identifier.getClass());
    }

    public Page<UserResponseDto> getAllUsers(int page, int size) {
        User authUser = this.getAuthenticatedUser();
        List<Organization> myOrgs = organizationRepository.findByCreatedBy_Id(authUser.getId());

        List<User> allUsersInMyOrgs = myOrgs.stream()
                .flatMap(org -> org.getUsers().stream())
                .distinct()
                .toList();

        int totalUsers = allUsersInMyOrgs.size();
        int fromIndex = page * size;

        if (fromIndex >= totalUsers) {
            return Page.empty(PageRequest.of(page, size));
        }

        int toIndex = Math.min(fromIndex + size, totalUsers);
        List<UserResponseDto> pagedUsers = allUsersInMyOrgs
                .stream().map(UserResponseDto::fromEntity).toList().subList(fromIndex, toIndex);

        return new PageImpl<>(pagedUsers, PageRequest.of(page, size), totalUsers);
    }

    protected User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return this.userRepository.findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Unauthorized user"));
        }
        return null;
    }
}
