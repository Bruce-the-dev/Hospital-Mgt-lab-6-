package com.hospital.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class DepartmentResponse {

    private Long id;
    private String name;
    private String location;

}

