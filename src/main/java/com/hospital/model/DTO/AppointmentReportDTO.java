package com.hospital.model.DTO;


import com.hospital.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentReportDTO {
    private Long appointmentId;
    private LocalDateTime appointmentDate;
    private Status status;
    private String relatedName; // doctor/patient
}

