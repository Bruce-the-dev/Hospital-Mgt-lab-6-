package com.hospital.model.mapper;

import com.hospital.model.DTO.PatientInput;
import com.hospital.model.DTO.PatientResponse;
import com.hospital.model.Patient;

public class PatientMapper {

        public static Patient toEntity(PatientInput dto) {
            Patient patient = new Patient();
            patient.setFirstName(dto.getFirstName());
            patient.setLastName(dto.getLastName());
            patient.setGender(dto.getGender());
            patient.setDob(dto.getDateOfBirth());
            patient.setPhone(dto.getPhone());
            patient.setEmail(dto.getEmail());
            return patient;
        }


    public static void updateEntity(PatientInput dto, Patient patient) {
        if (dto.getFirstName() != null) patient.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) patient.setLastName(dto.getLastName());
        if (dto.getGender() != null) patient.setGender(dto.getGender());
        if (dto.getDateOfBirth() != null) patient.setDob(dto.getDateOfBirth());
        if (dto.getPhone() != null) patient.setPhone(dto.getPhone());
        if (dto.getEmail() != null) patient.setEmail(dto.getEmail());
    }

    public static PatientResponse toResponse(Patient patient) {
        PatientResponse dto = new PatientResponse();
        dto.setId(patient.getPatientId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setGender(patient.getGender());
        dto.setDateOfBirth(patient.getDob());
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getEmail());
        return dto;
    }

}
