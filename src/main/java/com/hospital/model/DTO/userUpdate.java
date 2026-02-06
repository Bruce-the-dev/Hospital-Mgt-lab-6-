package com.hospital.model.DTO;

import com.hospital.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
    description = "Input payload used to create or update a user"
)

public class userUpdate {    
    @Schema(
        description = "User's user name",
        example = "John"
    )
private String userName;
@Schema( description = "password123")
private String password;
    @Schema(
        description = "User's full names with space",
        example = "John Doe"
    )
    private String fullName;
    @Schema(description = "User's Role should be one of  three", example = "ADMIN,RECEPTIONIST or DOCTOR")
    private Role role;
}
