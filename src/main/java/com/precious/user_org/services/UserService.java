package com.precious.user_org.services;

import com.precious.user_org.dto.auth.RegisterRequestDto;
import com.precious.user_org.exceptions.ResourceNotFoundException;
import com.precious.user_org.models.Organization;
import com.precious.user_org.models.Role;
import com.precious.user_org.models.User;
import com.precious.user_org.repositories.OrganizationRepository;
import com.precious.user_org.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(RegisterRequestDto registerRequestDto) {
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

        return this.userRepository.save(savedUser);
    }

    public User getUser(Object identifier) {
        if (identifier instanceof String email && email.contains("@")) {
            return this.userRepository.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else if (identifier instanceof UUID id) {
            return this.userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        throw new IllegalArgumentException("Unsupported identifier type: " + identifier.getClass());
    }

    public Page<User> getAllUsers(int page, int size) {
        return this.userRepository.findAll(PageRequest.of(page, size));
    }
}
