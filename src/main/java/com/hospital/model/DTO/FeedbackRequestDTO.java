package com.hospital.model.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FeedbackRequestDTO {

    @NotNull(message = "Patient id is required")
    private Long patientId;
    @NotNull(message = "Doctor id is required")
    private Long doctorId;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;
    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;
}
