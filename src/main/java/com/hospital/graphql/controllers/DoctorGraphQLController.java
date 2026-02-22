package com.hospital.graphql.controllers;

import com.hospital.model.DTO.DoctorInput;
import com.hospital.model.DTO.DoctorResponse;
import com.hospital.service.DoctorService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DoctorGraphQLController {

    private final DoctorService doctorService;

    // Queries

    @QueryMapping
    public DoctorResponse getDoctorById(Long id) {
        return doctorService.getDoctorById(id);
    }

    @QueryMapping
    public org.springframework.data.domain.Page<DoctorResponse> getAllDoctors(
            @org.springframework.graphql.data.method.annotation.Argument Integer page,
            @org.springframework.graphql.data.method.annotation.Argument Integer size) {
        return doctorService.getAllDoctors(page == null ? 0 : page, size == null ? 10 : size);
    }

    @QueryMapping
    public List<DoctorResponse> getDoctorsByDepartment(Long departmentId) {
        return doctorService.getDoctorsByDepartment(departmentId);
    }

    @MutationMapping
    public DoctorResponse createDoctor(DoctorInput input) {
        return doctorService.addDoctor(input);
    }

    @MutationMapping
    public DoctorResponse updateDoctor(Long id, DoctorInput input) {
        return doctorService.updateDoctor(id, input);
    }

    @MutationMapping
    public Boolean deleteDoctor(Long id) {
        doctorService.deleteDoctor(id);
        return true;
    }
}
