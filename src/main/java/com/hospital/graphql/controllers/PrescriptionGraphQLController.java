package com.hospital.graphql.controllers;

import com.hospital.model.DTO.PrescriptionCreateRequest;
import com.hospital.model.DTO.PrescriptionReportDTO;
import com.hospital.model.DTO.PrescriptionUpdateRequest;
import com.hospital.model.Prescription;
import com.hospital.service.PrescriptionService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PrescriptionGraphQLController {

    private final PrescriptionService prescriptionService;

    // Queries

    @QueryMapping
    public Prescription getPrescriptionById(Long id) {
        return prescriptionService.getPrescriptionById(id);
    }

    @QueryMapping
    public List<PrescriptionReportDTO> getAllPrescriptionReports(
            Integer size,
            Integer page,
            String sortBy,
            String direction) {
        return prescriptionService.getAllPrescriptionReports(
                size == null ? 10 : size,
                page == null ? 0 : page,
                sortBy == null ? "issuedDate" : sortBy,
                direction == null ? "asc" : direction);
    }

    @QueryMapping
    public List<PrescriptionReportDTO> getPrescriptionReportsByPatient(Long patientId) {
        return prescriptionService.getReportsByPatient(patientId);
    }

    @QueryMapping
    public List<PrescriptionReportDTO> getPrescriptionReportsByDoctor(Long doctorId) {
        return prescriptionService.getReportsByDoctor(doctorId);
    }

    // Mutations

    @MutationMapping
    public Prescription createPrescription(PrescriptionCreateRequest input) {
        return prescriptionService.createPrescription(input);
    }

    @MutationMapping
    public Prescription updatePrescription(Long id, PrescriptionUpdateRequest input) {
        return prescriptionService.updatePrescription(id, input);
    }

    @MutationMapping
    public Boolean deletePrescription(Long id) {
        prescriptionService.deletePrescription(id);
        return true;
    }
}
