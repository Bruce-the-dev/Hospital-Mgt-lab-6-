package com.hospital.graphql.controllers;

import com.hospital.model.DTO.AppointmentInput;
import com.hospital.model.DTO.AppointmentReportDTO;
import com.hospital.model.DTO.AppointmentResponse;
import com.hospital.model.DTO.FullAppointmentReportDTO;
import com.hospital.model.Status;
import com.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
public class AppointmentGraphQLController {

    private final AppointmentService appointmentService;

    @QueryMapping
    public AppointmentResponse getAppointmentById(@Argument Long id) {
        return appointmentService.getById(id);
    }

    @QueryMapping
    public org.springframework.data.domain.Page<AppointmentResponse> getAllAppointments(
            @org.springframework.graphql.data.method.annotation.Argument Integer page,
            @org.springframework.graphql.data.method.annotation.Argument Integer size) {
        return appointmentService.getAllAppointments(page == null ? 0 : page, size == null ? 10 : size);
    }

    @QueryMapping
    public List<AppointmentResponse> searchAppointmentsByStatus(@Argument Status status) {
        return appointmentService.searchByStatus(status);
    }

    @QueryMapping
    public List<AppointmentReportDTO> getAppointmentsByPatient(@Argument Long patientId) {
        return appointmentService.getAppointmentsByPatient(patientId, 0, 50);
    }

    @QueryMapping
    public List<AppointmentReportDTO> getAppointmentsByDoctor(@Argument Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId, 0, 50);
    }

    @QueryMapping
    public CompletableFuture<List<FullAppointmentReportDTO>> getFullAppointmentReport(@Argument int page,
            @Argument int size) {
        return appointmentService.getFullAppointmentReport(page, size);
    }

    @QueryMapping
    public List<FullAppointmentReportDTO> getAppointmentsWithoutPrescription() {
        return appointmentService.getAppointmentsWithoutPrescription();
    }

    @MutationMapping
    public AppointmentResponse addAppointment(@Argument AppointmentInput input) {
        return appointmentService.addAppointment(input);
    }

    @MutationMapping
    public AppointmentResponse updateAppointment(@Argument Long id, @Argument AppointmentInput input) {
        return appointmentService.updateAppointment(id, input);
    }

    @MutationMapping
    public Boolean deleteAppointment(@Argument Long id) {
        appointmentService.deleteAppointment(id);
        return true;
    }
}
