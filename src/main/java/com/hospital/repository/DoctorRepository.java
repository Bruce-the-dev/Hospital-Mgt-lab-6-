package com.hospital.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.hospital.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {

List<Doctor>findByDepartmentDepartmentId(Long departmentId);

//    Page<Doctor> findByLastNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(String name, String specialization, Pageable pageable);
}
