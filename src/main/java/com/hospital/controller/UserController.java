package com.hospital.controller;

import com.hospital.model.DTO.UserInput;
import com.hospital.model.DTO.UserResponse;
import com.hospital.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

        private final UserService userService;

        @GetMapping("/whoami")
        public ResponseEntity<?> whoami(Authentication auth) {
                return ResponseEntity.ok(auth.getAuthorities());
        }

        // Create user
        @Operation(summary = "Create a new user")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
        })
        @PostMapping
        public ResponseEntity<UserResponse> createUser(
                        @Valid @RequestBody UserInput input) {

                UserResponse created = userService.createUser(input);
                return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

        // Get user by ID
        @Operation(summary = "Get user by ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<UserResponse> getUserById(
                        @Parameter(description = "User ID") @PathVariable Long id) {

                return ResponseEntity.ok(userService.getUserById(id));
        }

        // Get user by username
        @Operation(summary = "Get user by username")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
        })
        @GetMapping("/by-username/{username}")
        public ResponseEntity<UserResponse> getUserByUsername(
                        @Parameter(description = "Username") @PathVariable String username) {

                return ResponseEntity.ok(userService.getUserByUsername(username));
        }

        // Get all users (paged)
        @Operation(summary = "Get all users with pagination and sorting")
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
        @GetMapping
        public ResponseEntity<Page<UserResponse>> getAllUsers(
                        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

                        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,

                        @Parameter(description = "Sort by field") @RequestParam(defaultValue = "username") String sortBy,

                        @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String direction) {

                return ResponseEntity.ok(
                                userService.getAllUsers(page, size, sortBy, direction));
        }

        // Update user
        @Operation(summary = "Update an existing user")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
        })
        @PutMapping("/{id}")
        public ResponseEntity<UserResponse> updateUser(
                        @Parameter(description = "User ID") @PathVariable Long id,
                        @Valid @RequestBody UserInput input) {

                return ResponseEntity.ok(userService.updateUser(id, input));
        }

        // Enable user
        @Operation(summary = "Enable a user")
        @ApiResponse(responseCode = "204", description = "User enabled successfully")
        @PatchMapping("/{id}/enable")
        public ResponseEntity<Void> enableUser(
                        @Parameter(description = "User ID") @PathVariable Long id) {

                userService.enableUser(id);
                return ResponseEntity.noContent().build();
        }

        // Disable user
        @Operation(summary = "Disable a user")
        @ApiResponse(responseCode = "204", description = "User disabled successfully")
        @PatchMapping("/{id}/disable")
        public ResponseEntity<Void> disableUser(
                        @Parameter(description = "User ID") @PathVariable Long id) {

                userService.disableUser(id);
                return ResponseEntity.noContent().build();
        }

        // Delete user
        @Operation(summary = "Delete a user")
        @ApiResponse(responseCode = "204", description = "User deleted successfully")
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(
                        @Parameter(description = "User ID") @PathVariable Long id) {

                userService.deleteUser(id);
                return ResponseEntity.noContent().build();
        }

}
