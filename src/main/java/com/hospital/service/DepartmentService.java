package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.DTO.DepartmentInput;
import com.hospital.model.DTO.DepartmentResponse;
import com.hospital.model.Department;
import com.hospital.model.mapper.DepartmentMapper;
import com.hospital.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @CacheEvict(value = "departments", allEntries = true)
    public DepartmentResponse addDepartment(DepartmentInput input) {
        Department department = DepartmentMapper.toEntity(input);
        Department saved = departmentRepository.save(department);
        return DepartmentMapper.toResponse(saved);
    }
    @Cacheable(value = "departments", key = "#id")
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
        return DepartmentMapper.toResponse(department);
    }
    @Cacheable(value = "departments")
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toResponse)
                .collect(Collectors.toList());
    }
    @CacheEvict(value = "departments", allEntries = true)
    public DepartmentResponse updateDepartment(Long id, DepartmentInput input) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));

        DepartmentMapper.updateEntity(input, department);
        Department updated = departmentRepository.save(department);
        return DepartmentMapper.toResponse(updated);
    }
    @CacheEvict(value = "departments", allEntries = true)
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id " + id);
        }
        departmentRepository.deleteById(id);
    }

}
