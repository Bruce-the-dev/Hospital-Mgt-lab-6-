
package com.hospital.graphql.controllers;

import com.hospital.model.DTO.MedicationCreateRequest;
import com.hospital.model.DTO.MedicationUpdateRequest;
import com.hospital.model.Medication;
import com.hospital.service.MedicationService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicationGraphQLController {

    private final MedicationService medicationService;

    @QueryMapping
    public Medication getMedicationById(Long id) {
        return medicationService.getMedicationById(id);
    }

    @QueryMapping
    public List<Medication> getAllMedications(
            Integer page,
            Integer size,
            String sortBy,
            String direction
    ) {
        return medicationService.getMedicationsPage(
                page == null ? 0 : page,
                size == null ? 10 : size,
                sortBy == null ? "medicationId" : sortBy,
                direction == null ? "asc" : direction
        ).getContent();
    }

    @MutationMapping
    public Medication createMedication(MedicationCreateRequest input) {
        return medicationService.addMedication(input);
    }

    @MutationMapping
    public Medication updateMedication(Long id, MedicationUpdateRequest input) {
        return medicationService.updateMedication(id, input);
    }

    @MutationMapping
    public Boolean deleteMedication(Long id) {
        medicationService.deleteMedication(id);
        return true;
    }
}
