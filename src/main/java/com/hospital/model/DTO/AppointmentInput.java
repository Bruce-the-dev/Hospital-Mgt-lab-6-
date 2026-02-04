package com.hospital.model.DTO;

import com.hospital.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentInput {

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private LocalDateTime appointmentDate;

    @NotNull
    private Status status;
}

