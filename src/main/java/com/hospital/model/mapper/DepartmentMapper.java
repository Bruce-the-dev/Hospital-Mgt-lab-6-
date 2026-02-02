package com.hospital.model.mapper;

import com.hospital.model.DTO.DepartmentInput;
import com.hospital.model.DTO.DepartmentResponse;
import com.hospital.model.Department;

public class DepartmentMapper {

    public static Department toEntity(DepartmentInput dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setLocation(dto.getLocation());
        return department;
    }

    public static void updateEntity(DepartmentInput dto, Department department) {
        if (dto.getName() != null) department.setName(dto.getName());
        if (dto.getLocation() != null) department.setLocation(dto.getLocation());
    }

    public static DepartmentResponse toResponse(Department department) {
        DepartmentResponse dto = new DepartmentResponse();
        dto.setId(department.getDepartmentId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());
        return dto;
    }
}

