package com.hospital.graphql.controllers;

import com.hospital.model.DTO.DepartmentInput;
import com.hospital.model.DTO.DepartmentResponse;
import com.hospital.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DepartmentGraphQLController {

    private final DepartmentService departmentService;

    // Queries

    @QueryMapping
    public DepartmentResponse getDepartmentById(Long id) {
        return departmentService.getDepartmentById(id);
    }

    @QueryMapping
    public List<DepartmentResponse> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    // Mutations

    @MutationMapping
    public DepartmentResponse createDepartment(DepartmentInput input) {
        return departmentService.addDepartment(input);
    }

    @MutationMapping
    public DepartmentResponse updateDepartment(Long id, DepartmentInput input) {
        return departmentService.updateDepartment(id, input);
    }

    @MutationMapping
    public Boolean deleteDepartment(Long id) {
        departmentService.deleteDepartment(id);
        return true;
    }
}
