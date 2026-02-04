package com.hospital.specification;

import com.hospital.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {
    public static Specification<Doctor> hasLastName(String name) {
        return (root, query, cb) ->
                name != null && !name.isBlank() ?
                        cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%") : null;

    }

    public static Specification<Doctor> hasSpecialization(String specialization) {

        return (root, query, cb) ->
                specialization != null && !specialization.isBlank() ?
                        cb.like(cb.lower(root.get("specialization")), "%" + specialization.toLowerCase() + "%") : null;

    }

    public static Specification<Doctor> hasDepartment(Long departmentId) {
        return (root, query, cb) ->
                departmentId != null ? cb.equal(root.get("department").get("departmentId"), departmentId) : null;
    }

}
