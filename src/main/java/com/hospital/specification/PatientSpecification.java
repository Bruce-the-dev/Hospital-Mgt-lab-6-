package com.hospital.specification;

import com.hospital.model.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PatientSpecification {

    public static Specification<Patient> hasLastName(String lastName) {
        return (root, query, cb) ->
                lastName == null || lastName.isBlank()
                        ? null
                        : cb.like(
                        cb.lower(root.get("lastName")),
                        "%" + lastName.toLowerCase() + "%"
                );
    }

    public static Specification<Patient> hasGender(Character gender) {
        return (root, query, cb) ->
                gender == null
                        ? null
                        : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Patient> bornAfter(LocalDate date) {
        return (root, query, cb) ->
                date == null
                        ? null
                        : cb.greaterThan(root.get("dob"), date);
    }
}

