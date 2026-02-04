package com.hospital.model.DTO;

import com.hospital.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private Status status;
}

