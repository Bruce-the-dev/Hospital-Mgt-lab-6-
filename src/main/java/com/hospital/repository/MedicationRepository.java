package com.hospital.repository;

import com.hospital.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository
        extends JpaRepository<Medication, Long> {

    boolean existsByNameIgnoreCase(String name);

}
