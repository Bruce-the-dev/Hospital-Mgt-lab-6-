package com.hospital.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DepartmentInput {

    @NotBlank(message = "Department name is required")
    private String name;

    @NotBlank(message = "Department location is required")
    private String location;

}

