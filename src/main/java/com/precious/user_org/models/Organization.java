package com.precious.user_org.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(exclude = {"users"})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by_id", referencedColumnName = "id")
    @JsonIgnore
    private User createdBy;

    @ManyToMany(mappedBy = "organizations")
    @JsonBackReference("user-organizations")
    private Set<User> users = new HashSet<>();
}
