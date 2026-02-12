package com.hospital.controller;

import com.hospital.model.DTO.AppointmentInput;
import com.hospital.model.DTO.AppointmentReportDTO;
import com.hospital.model.DTO.AppointmentResponse;
import com.hospital.model.DTO.FullAppointmentReportDTO;
import com.hospital.model.Status;
import com.hospital.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment management and reports")
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody AppointmentInput appointment
    ) {
        AppointmentResponse saved = appointmentService.addAppointment(appointment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','RECEPTIONIST','ADMIN')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentInput appointment
    ) {
        return ResponseEntity.ok(
                appointmentService.updateAppointment(id, appointment)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }


    // Appointments for a specific patient
    @Operation(
            summary = "Get appointments for a patient",
            description = "Returns paginated appointment history for a given patient"
    )
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<AppointmentReportDTO>> getAppointmentsByPatient(
            @Parameter(description = "Patient ID", example = "1")
            @PathVariable Long patientId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatient(patientId, page, size)
        );
    }

    // Appointments for a specific doctor
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<AppointmentReportDTO>> getAppointmentsByDoctor(
            @Parameter(description = "Doctor's ID", example = "1")
            @PathVariable Long doctorId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctor(doctorId, page, size)
        );
    }

    // Full appointment report (admin view)
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','RECEPTIONIST','ADMIN')")
    @GetMapping("/reports/full")
    public ResponseEntity<List<FullAppointmentReportDTO>> getFullAppointmentReport(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                appointmentService.getFullAppointmentReport(size, page)
        );
    }

    // Appointments without prescription
    @GetMapping("/reports/without-prescription")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<FullAppointmentReportDTO>> getAppointmentsWithoutPrescription() {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsWithoutPrescription()
        );
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<AppointmentResponse>> getByStatus(
            @PathVariable Status status
    ) {
        return ResponseEntity.ok(
                appointmentService.searchByStatus(status)
        );
    }
    @GetMapping("/sortbyDate")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<AppointmentResponse>> sortByDate() {
        return ResponseEntity.ok(appointmentService.sortByDate());
    }
}
