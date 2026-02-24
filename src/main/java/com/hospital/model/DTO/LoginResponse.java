package com.hospital.model.DTO;

import lombok.Data;


@Data
public class LoginResponse {

    private String token;
    private String tokenType = "Bearer";
    private long expiresIn;

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
