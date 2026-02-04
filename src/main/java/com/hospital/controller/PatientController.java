package com.hospital.controller;

import com.hospital.model.DTO.PatientInput;
import com.hospital.model.DTO.PatientResponse;
import com.hospital.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Add a new patient")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<PatientResponse> create(
            @Valid @RequestBody PatientInput input) {

        PatientResponse response = patientService.addPatient(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get patient by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id) {

        PatientResponse response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all patients")
    @ApiResponse(responseCode = "200", description = "Patients retrieved successfully")
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll() {

        return ResponseEntity.ok(patientService.getAllPatients());
    }


    @Operation(summary = "Update patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PatientInput input) {

        PatientResponse response = patientService.updatePatient(id, input);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Delete patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

//search+ pagination
    @Operation(summary = "Search patients with filters")
    @ApiResponse(responseCode = "200", description = "Patients retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<PatientResponse>> search(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Character gender,
            @RequestParam(required = false) LocalDate bornAfter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<PatientResponse> result = patientService.searchPatients(
                lastName, gender, bornAfter, page, size, sortBy, direction
        );

        return ResponseEntity.ok(result);
    }
}
