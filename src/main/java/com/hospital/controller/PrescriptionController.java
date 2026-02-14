package com.hospital.controller;

import com.hospital.model.DTO.PrescriptionCreateRequest;
import com.hospital.model.DTO.PrescriptionReportDTO;
import com.hospital.model.DTO.PrescriptionUpdateRequest;
import com.hospital.model.Prescription;
import com.hospital.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescriptions", description = "Prescription management APIs")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Operation(
            summary = "Create a new prescription",
            description = "Creates a prescription for an existing appointment",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Prescription created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public ResponseEntity<Prescription> createPrescription(
            @Valid @RequestBody PrescriptionCreateRequest request
    ) {
        Prescription prescription = prescriptionService.createPrescription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(prescription);
    }

    @Operation(
            summary = "Get prescription by ID",
            description = "Fetch a prescription by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Prescription found"),
                    @ApiResponse(responseCode = "404", description = "Prescription not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public ResponseEntity<Prescription> getPrescriptionById(
            @Parameter(description = "Prescription ID", example = "1")
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @Operation(
            summary = "Update prescription",
            description = "Update issued date, notes, or medications of a prescription",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Prescription updated"),
                    @ApiResponse(responseCode = "404", description = "Prescription not found", content = @Content)
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public ResponseEntity<Prescription> updatePrescription(
            @Parameter(description = "Prescription ID", example = "1")
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody PrescriptionUpdateRequest request
    ) {
        return ResponseEntity.ok(
                prescriptionService.updatePrescription(id, request)
        );
    }

    @Operation(
            summary = "Delete prescription",
            description = "Delete a prescription by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Prescription deleted"),
                    @ApiResponse(responseCode = "404", description = "Prescription not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public void deletePrescription(
            @Parameter(description = "Prescription ID", example = "1")
            @PathVariable @Min(1) Long id
    ) {
        prescriptionService.deletePrescription(id);
    }

    @Operation(
            summary = "Get all prescription reports (paginated)",
            description = "Returns paginated prescription reports with sorting support"
    )
    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
    public ResponseEntity<List<PrescriptionReportDTO>> getAllReports(
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "issuedDate")  String sortBy,
            @RequestParam(defaultValue = "asc")  String direction
    ) {
        return ResponseEntity.ok(
                prescriptionService.getAllPrescriptionReports(size, page, sortBy, direction)
        );
    }

    @Operation(
            summary = "Get prescription reports by patient",
            description = "Fetch all prescription reports for a specific patient"
    )
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
    @GetMapping("/reports/patient/{patientId}")
    public ResponseEntity<List<PrescriptionReportDTO>> getReportsByPatient(
            @Parameter(description = "Patient ID", example = "5")
            @PathVariable @Min(1) Long patientId
    ) {
        return ResponseEntity.ok(
                prescriptionService.getReportsByPatient(patientId)
        );
    }

    @Operation(
            summary = "Get prescription reports by doctor",
            description = "Fetch all prescription reports written by a specific doctor"
    )
    @GetMapping("/reports/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
    public ResponseEntity<List<PrescriptionReportDTO>> getReportsByDoctor(
            @Parameter(description = "Doctor ID", example = "3")
            @PathVariable @Min(1) Long doctorId
    ) {
        return ResponseEntity.ok(
                prescriptionService.getReportsByDoctor(doctorId)
        );
    }
}
