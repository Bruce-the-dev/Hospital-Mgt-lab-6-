package com.hospital.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackResponseDTO {

    private Long feedbackId;
    private Long patientId;
    private Long doctorId;
    private int rating;
    private String comment;
    private LocalDateTime createdDate;
}
