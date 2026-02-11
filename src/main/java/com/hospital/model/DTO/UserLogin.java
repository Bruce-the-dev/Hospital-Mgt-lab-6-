package com.hospital.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Schema(
        name = "UserLogin",
        description = "DTO representing login credentials for a user"
)
public class UserLogin {

    @Schema(
            description = "Username of the user",
            example = "john.doe"
    )
    @NotEmpty(message = "Username cannot be empty")
    private String userName;

    @Schema(
            description = "Password of the user",
            example = "P@ssw0rd123"
    )
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
