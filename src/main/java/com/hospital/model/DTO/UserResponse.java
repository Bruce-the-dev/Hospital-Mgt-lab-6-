package com.hospital.model.DTO;

import com.hospital.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String username;
    private String fullName;
    private Role role;
    private boolean status;
    private LocalDateTime createdAt;
    private String email;
}
