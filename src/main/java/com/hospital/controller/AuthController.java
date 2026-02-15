package com.hospital.controller;

import com.hospital.config.JwtService;
import com.hospital.model.DTO.LoginResponse;
import com.hospital.model.DTO.UserInput;
import com.hospital.model.DTO.UserLogin;
import com.hospital.model.DTO.UserResponse;
import com.hospital.service.TokenBlacklistService;
import com.hospital.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController

@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final UserService userService;
        private final TokenBlacklistService tokenBlacklistService;

        // Authenticate user (Login)
        @Operation(summary = "Authenticate user", description = "Authenticates a user using username and password")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Login successful"),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials")
        })
        @PostMapping("/auth/login")
        public ResponseEntity<LoginResponse> login(@RequestBody UserLogin request) {
                System.out.println("Attempting login for user: " + request.getUserName());
                try {
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getUserName(),
                                                        request.getPassword()));

                        UserDetails user = (UserDetails) authentication.getPrincipal();
                        String token = jwtService.generateToken(user);
                        System.out.println("Login successful for user: " + request.getUserName());
                        return ResponseEntity.ok(new LoginResponse(token, jwtService.getExpiration()));
                } catch (Exception e) {
                        System.out.println("Login failed for user: " + request.getUserName() + " Reason: "
                                        + e.getMessage());
                        throw e;
                }
        }

        @Operation(summary = "Logout user", description = "Invalidates the current JWT token")
        @PostMapping("/auth/logout")
        public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
                if (token != null && token.startsWith("Bearer ")) {
                        String jwt = token.substring(7);
                        tokenBlacklistService.blacklistToken(jwt);
                        System.out.println("Logout successful. Token blacklisted.");
                        return ResponseEntity.ok("Logged out successfully");
                }
                return ResponseEntity.badRequest().body("Invalid token");
        }

        @Operation(summary = "Create a new user")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
        })
        @PostMapping("/auth")
        public ResponseEntity<UserResponse> createUser(
                        @Valid @RequestBody UserInput input) {

                UserResponse created = userService.createUser(input);
                return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

        @GetMapping("/auth/me")
        public Map<String, Object> me(Authentication authentication) {

                if (authentication == null || !authentication.isAuthenticated()
                                || Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
                }

                Map<String, Object> response = new HashMap<>();
                response.put("principal", authentication.getPrincipal());
                response.put("authorities", authentication.getAuthorities());

                return response;
        }

        @GetMapping("/")
        public String home() {
                return "Login successful. Welcome to Hospital Management System!";
        }
}
