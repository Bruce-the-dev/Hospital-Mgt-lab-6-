package com.hospital.model.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Setter
public class PatientResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private Character gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;

}

