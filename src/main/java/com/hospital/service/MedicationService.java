package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.DTO.MedicationCreateRequest;
import com.hospital.model.DTO.MedicationUpdateRequest;
import com.hospital.model.Medication;
import com.hospital.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @CacheEvict(value = "medications", allEntries = true)
    public Medication addMedication(MedicationCreateRequest request) {
        if (medicationRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Medication with this name already exists");
        }

        Medication medication = new Medication();
        medication.setName(request.getName());
        medication.setDescription(request.getDescription());

        return medicationRepository.save(medication);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "medications", key = "#id")
    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medication not found with id " + id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "medications")
    public Page<Medication> getAllMedications(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending()
        );
        return medicationRepository.findAll(pageable);
    }

    @CacheEvict(value = "medications", allEntries = true)
    public Medication updateMedication(Long id, MedicationUpdateRequest request) {
        Medication m = medicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medication not found with id " + id));
        if (request.getName() != null && !request.getName().equalsIgnoreCase(m.getName())) {
            if (medicationRepository.existsByNameIgnoreCase(request.getName())) {
                throw new IllegalArgumentException("Medication with this name already exists");
            }
            m.setName(request.getName());
        }
        if (request.getDescription() != null) {
            m.setDescription(request.getDescription());
        }

        return m; // managed entity → auto UPDATE
    }

    @CacheEvict(value = "medications", allEntries = true)
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medication not found with id " + id);
        }
        medicationRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Page<Medication> getMedicationsPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return medicationRepository.findAll(pageable);
    }

}
