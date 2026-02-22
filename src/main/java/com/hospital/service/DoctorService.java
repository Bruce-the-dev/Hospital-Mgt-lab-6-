package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.DTO.DoctorInput;
import com.hospital.model.DTO.DoctorResponse;
import com.hospital.model.Department;
import com.hospital.model.Doctor;
import com.hospital.model.mapper.DoctorMapper;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.specification.DoctorSpecification;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @CacheEvict(value = "doctors", allEntries = true)
    public DoctorResponse addDoctor(DoctorInput input) {
        Department department = departmentRepository.findById(input.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id " + input.getDepartmentId()));

        Doctor doctor = DoctorMapper.toEntity(input, department);

        Doctor saved = doctorRepository.save(doctor); // This works but strictly speaking, it’s redundant.
        // Because: doctor is already managed, Transaction commit → flush

        return DoctorMapper.toResponse(saved);
    }

    @Cacheable(value = "doctors", key = "#id")
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + id));
        return DoctorMapper.toResponse(doctor);
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> getAllDoctors(int page, int size) {
        return doctorRepository.findAll(PageRequest.of(page, size))
                .map(DoctorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentDepartmentId(departmentId)
                .stream()
                .map(DoctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "doctors", key = "#id")
    public DoctorResponse updateDoctor(Long id, DoctorInput input) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + id));

        Department department = null;
        if (input.getDepartmentId() != null) {
            department = departmentRepository.findById(input.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department not found with id " + input.getDepartmentId()));
        }

        DoctorMapper.updateEntity(input, doctor, department);
        Doctor updated = doctorRepository.save(doctor);
        return DoctorMapper.toResponse(updated);
    }

    @CacheEvict(value = "doctors", key = "#id")
    public void deleteDoctor(Long id) {
        Doctor doc = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + id));

        doctorRepository.delete(doc);
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> searchDoctors(
            String name, String specialization, int page, int size, String sortBy, String direction) {

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Specification<Doctor> spec = Specification.where((Specification<Doctor>) null);

        if (name != null && !name.isBlank()) {
            spec = spec.and(DoctorSpecification.hasLastName(name));
        }

        if (specialization != null && !specialization.isBlank()) {
            spec = spec.and(DoctorSpecification.hasSpecialization(specialization));
        }

        return doctorRepository.findAll(spec, pageable).map(DoctorMapper::toResponse);
    }

}
