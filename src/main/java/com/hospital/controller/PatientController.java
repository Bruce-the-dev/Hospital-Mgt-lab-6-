package com.hospital.controller;

import com.hospital.model.DTO.PatientInput;
import com.hospital.model.DTO.PatientResponse;
import com.hospital.model.mapper.PatientMapper;
import com.hospital.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> create(
            @Valid @RequestBody PatientInput input) {
        return ResponseEntity.ok(patientService.addPatient(input));
    }

}
