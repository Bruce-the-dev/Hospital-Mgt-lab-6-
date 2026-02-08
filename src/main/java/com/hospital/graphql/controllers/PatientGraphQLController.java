package com.hospital.graphql.controllers;

import com.hospital.model.DTO.PatientInput;
import com.hospital.model.DTO.PatientResponse;
import com.hospital.service.PatientService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PatientGraphQLController {

    private final PatientService patientService;

    // Queries

    @QueryMapping
    public PatientResponse getPatientById(Long id) {
        return patientService.getPatientById(id);
    }

    @QueryMapping
    public Page<PatientResponse> getPatientsByPage(
            Integer page,
            Integer size,
            String sortBy,
            String direction
    ) {
        return patientService.getPatientsByPage(
                page == null ? 0 : page,
                size == null ? 10 : size,
                sortBy == null ? "id" : sortBy,
                direction == null ? "asc" : direction
        );
    }

    @QueryMapping
    public List<PatientResponse> searchPatients(String lastName, String gender, String bornAfter, Integer page, Integer size, String sortBy, String direction) {
        return patientService.searchPatients(
                lastName,
                gender == null ? null : gender.charAt(0),
                bornAfter == null ? null : LocalDate.parse(bornAfter),
                page ==null ?0  : page,
                size==null?10:size,
                sortBy,
                direction
        ).getContent();
    }

    @MutationMapping
    public PatientResponse createPatient(PatientInput input) {
        return patientService.addPatient(input);
    }

    @MutationMapping
    public PatientResponse updatePatient(Long id, PatientInput input) {
        return patientService.updatePatient(id, input);
    }

    @MutationMapping
    public Boolean deletePatient(Long id) {
        patientService.deletePatient(id);
        return true;
    }
}
