package com.hospital.repository;

import com.hospital.model.PrescriptionMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionMedicationRepository
        extends JpaRepository<PrescriptionMedication, Long> {

    List<PrescriptionMedication> findByPrescriptionPrescriptionId(Long prescriptionId);

    void deleteByPrescriptionPrescriptionId(Long prescriptionId);
}

