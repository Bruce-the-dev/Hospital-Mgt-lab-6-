package com.hospital.controller;

import com.hospital.model.DTO.MedicationCreateRequest;
import com.hospital.model.DTO.MedicationUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hospital.model.Medication;
import com.hospital.service.MedicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
@Tag(name = "Medication API", description = "CRUD operations for medications")
public class MedicationController {

    private final MedicationService medicationService;

    @Operation(summary = "Create a new medication", description = "Adds a new medication with name and description")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medication created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<Medication> createMedication(@Valid @RequestBody MedicationCreateRequest request) {
        if (request.getName() == null || request.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }

        Medication savedMedication = medicationService.addMedication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedication);
    }

    @Operation(summary = "Get all medications")
    @ApiResponse(responseCode = "200", description = "List of medications")
    @GetMapping("/all")
    public ResponseEntity<Page<Medication>> getAllMedications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "medicationId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Medication> output = medicationService.getMedicationsPage(page, size, sortBy, direction);
        return ResponseEntity.ok(output);
    }

    @Operation(summary = "Get medication by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medication found"),
            @ApiResponse(responseCode = "404", description = "Medication not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        Medication output = medicationService.getMedicationById(id);
        return ResponseEntity.ok(output);
    }

    @Operation(summary = "Update medication by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medication updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Medication not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(
            @PathVariable Long id,
           @Valid    @RequestBody MedicationUpdateRequest request
    ) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }

        Medication updatedMedication = medicationService.updateMedication(id, request);
        return ResponseEntity.ok(updatedMedication);
    }

    @Operation(summary = "Delete medication by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medication deleted"),
            @ApiResponse(responseCode = "404", description = "Medication not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
