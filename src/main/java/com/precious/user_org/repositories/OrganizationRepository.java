package com.precious.user_org.repositories;

import com.precious.user_org.models.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    List<Organization> findByCreatedBy_Id(UUID createdById);

    @Query("SELECT o FROM Organization o JOIN o.users u WHERE u.id = :userId")
    Page<Organization> findByUserMember(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT o FROM Organization o JOIN o.users u WHERE u.id = :userId")
    List<Organization> findByUserMember(@Param("userId") UUID userId);
}
