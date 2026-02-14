package com.hospital.repository;

import com.hospital.model.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicationRepository
        extends JpaRepository<Medication, Long> {


    boolean existsByNameIgnoreCase(String name);

}

