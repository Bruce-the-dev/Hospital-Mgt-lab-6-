package com.hospital.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorInput {

    @NotBlank(message = "Doctor's first name is required")
    private String firstName;

    @NotBlank(message = "Doctor's second name is required")
    private String lastName;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

}

