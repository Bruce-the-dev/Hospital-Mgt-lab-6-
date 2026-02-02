package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.DTO.DoctorInput;
import com.hospital.model.DTO.DoctorResponse;
import com.hospital.model.Department;
import com.hospital.model.Doctor;
import com.hospital.model.mapper.DoctorMapper;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
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

    public DoctorResponse addDoctor(DoctorInput input) {
        Department department = departmentRepository.findById(input.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id " + input.getDepartmentId()));

        Doctor doctor = DoctorMapper.toEntity(input, department);
        Doctor saved = doctorRepository.save(doctor);
        return DoctorMapper.toResponse(saved);
    }
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + id));
        return DoctorMapper.toResponse(doctor);
    }
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(DoctorMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentDepartmentId(departmentId)
                .stream()
                .map(DoctorMapper::toResponse)
                .collect(Collectors.toList());
    }
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
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with id " + id);
        }
        doctorRepository.deleteById(id);
    }
}
