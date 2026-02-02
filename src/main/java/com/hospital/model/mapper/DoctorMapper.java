package com.hospital.model.mapper;

import com.hospital.model.DTO.DoctorInput;
import com.hospital.model.DTO.DoctorResponse;
import com.hospital.model.Department;
import com.hospital.model.Doctor;

public class DoctorMapper {

    public static Doctor toEntity(DoctorInput dto, Department department) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setDepartment(department);
        return doctor;
    }

    public static void updateEntity(DoctorInput dto, Doctor doctor, Department department) {
        if (dto.getFirstName() != null) doctor.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) doctor.setLastName(dto.getLastName());
        if (dto.getSpecialization() != null) doctor.setSpecialization(dto.getSpecialization());
        if (department != null) doctor.setDepartment(department);
    }

    public static DoctorResponse toResponse(Doctor doctor) {
        DoctorResponse dto = new DoctorResponse();
        dto.setId(doctor.getDoctorId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());

        if (doctor.getDepartment() != null) {
            dto.setDepartmentId(doctor.getDepartment().getDepartmentId());
            dto.setDepartmentName(doctor.getDepartment().getName());
        }

        return dto;
    }
}

