package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_users_username", columnList = "username"),
                @Index(name = "idx_users_role", columnList = "role"),
                @Index(name = "idx_users_status", columnList = "status")})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(length = 100)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean status = true;

    @Column(nullable = false, updatable = false)

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}


