package com.hospital.model.DTO;

import lombok.Data;

@Data
public class DoctorResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;

    private Long departmentId;
    private String departmentName;


}

