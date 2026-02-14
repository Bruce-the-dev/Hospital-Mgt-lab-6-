package com.hospital.model.mapper;

import com.hospital.model.DTO.UserInput;
import com.hospital.model.DTO.UserResponse;
import com.hospital.model.User;

public class UserMapper {

    private UserMapper() {
        // utility class
    }

    public static User toEntity(UserInput input) {
        User user = new User();
        user.setUsername(input.getUsername());
        user.setFullName(input.getFullName());
        user.setPassword(input.getPassword()); // hashed later in service
        user.setRole(input.getRole());
        user.setEmail(input.getEmail());

        if (input.getStatus() != null) {
            user.setStatus(input.getStatus());
        }

        return user;
    }

    public static void updateEntity(UserInput input, User user) {

        if (input.getUsername() != null) {
            user.setUsername(input.getUsername());
        }

        if (input.getFullName() != null) {
            user.setFullName(input.getFullName());
        }

        if (input.getPassword() != null) {
            user.setPassword(input.getPassword()); // hashed later
        }

        if (input.getRole() != null) {
            user.setRole(input.getRole());
        }
        if (input.getEmail() != null) {
            user.setEmail(input.getEmail());
        }

        if (input.getStatus() != null) {
            user.setStatus(input.getStatus());
        }
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.isStatus(),
                user.getCreatedAt(),
                user.getEmail()
        );
    }
}
