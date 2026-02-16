package com.hospital.controller;

import com.hospital.model.DTO.FeedbackRequestDTO;
import com.hospital.model.DTO.FeedbackResponseDTO;
import com.hospital.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Feedback management APIs")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Create feedback", responses = {
            @ApiResponse(responseCode = "201", description = "Feedback created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping

    public ResponseEntity<FeedbackResponseDTO> create(
            @Valid @RequestBody FeedbackRequestDTO dto) {
        return new ResponseEntity<>(feedbackService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get feedback by ID")
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getById(
            @Parameter(description = "Feedback ID") @PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getById(id));
    }

    @Operation(summary = "Get all feedback (paginated)")
    @GetMapping
    public ResponseEntity<Page<FeedbackResponseDTO>> getAll(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(feedbackService.getAll(pageable));
    }

    @Operation(summary = "Get feedback by doctor (paginated)")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<FeedbackResponseDTO>> getByDoctor(
            @PathVariable Long doctorId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(feedbackService.getByDoctor(doctorId, pageable));
    }

    @Operation(summary = "Get feedback by patient (paginated)")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<FeedbackResponseDTO>> getByPatient(
            @PathVariable Long patientId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(feedbackService.getByPatient(patientId, pageable));
    }

    @Operation(summary = "Update feedback")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO dto) {
        return ResponseEntity.ok(feedbackService.update(id, dto));
    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feedbackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
