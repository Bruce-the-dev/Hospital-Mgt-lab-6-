package com.hospital.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionMedicationRequest {

    @NotNull(message = "Medication ID is required")
    private Long medicationId;

    @NotBlank(message = "Dosage is required")
    @Size(max = 100)
    private String dosage;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
