package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.DTO.PatientInput;
import com.hospital.model.DTO.PatientResponse;
import com.hospital.model.Patient;
import com.hospital.model.mapper.PatientMapper;
import com.hospital.repository.PatientRepository;
import com.hospital.specification.PatientSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponse addPatient(PatientInput input) {
        // save() = INSERT when id is null
        Patient patient = PatientMapper.toEntity(input);
        Patient saved = patientRepository.save(patient);
        return PatientMapper.toResponse(saved);
    }

    @CacheEvict(value = "patients", key = "#id")
    public PatientResponse updatePatient(Long id, PatientInput updatedData) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
        // save() = UPDATE because entity is managed
        PatientMapper.updateEntity(updatedData, p);
        return PatientMapper.toResponse(p);
    }

    @CacheEvict(value = "patients", key = "#id")
    public void deletePatient(Long id) {
        patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
        patientRepository.deleteById(id);
    }

    @Cacheable(value = "patients", key = "#id")
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        return patientRepository.findById(id).map(PatientMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> searchPatients(String lastName, Character gender,
            LocalDate bornAfter, int number, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(number, size, sort);

        Specification<Patient> spec = Specification.where(PatientSpecification.hasLastName(lastName))
                .and(PatientSpecification.hasGender(gender)).and(PatientSpecification.bornAfter(bornAfter));

        return patientRepository.findAll(spec, pageable).map(PatientMapper::toResponse);
    }

    public Page<PatientResponse> getPatientsByPage(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC));
        return patientRepository.findAll(pageable).map(PatientMapper::toResponse);
    }
}
