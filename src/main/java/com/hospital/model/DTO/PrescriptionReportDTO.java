package com.hospital.model.DTO;

import java.time.LocalDate;

public record PrescriptionReportDTO(
        Long prescriptionId,
        Long appointmentId,
        String patientName,
        String doctorName,
        String medicationName,
        String dosage,
        LocalDate issuedDate
) {}
