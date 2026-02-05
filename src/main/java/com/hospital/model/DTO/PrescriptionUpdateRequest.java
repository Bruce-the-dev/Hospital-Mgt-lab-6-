package com.hospital.model.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionUpdateRequest {

    @PastOrPresent(message = "Issued date cannot be in the future")
    private LocalDate issuedDate;

    @Size(max = 500)
    private String notes;

    @Valid
    private List<PrescriptionMedicationRequest> medications;
}
