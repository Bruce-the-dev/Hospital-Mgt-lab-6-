package com.hospital.repository;

import com.hospital.model.DTO.PrescriptionReportDTO;
import com.hospital.model.Prescription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {

    @Query("""
    SELECT new com.hospital.model.DTO.PrescriptionReportDTO(
        p.prescriptionId,
        a.appointmentId,
        CONCAT(pat.firstName, ' ', pat.lastName),
        CONCAT(d.firstName, ' ', d.lastName),
        m.name,
        pm.dosage,
        p.issuedDate
    )
    FROM Prescription p
    JOIN p.appointment a
    JOIN a.patient pat
    JOIN a.doctor d
    JOIN p.prescriptionMedications pm
    JOIN pm.medication m
    ORDER BY p.issuedDate DESC
""")
    List<PrescriptionReportDTO> findAllPrescriptionReports(Pageable pageable);

    @Query("""
    SELECT new com.hospital.model.DTO.PrescriptionReportDTO(
        p.prescriptionId,
        a.appointmentId,
        CONCAT(pat.firstName, ' ', pat.lastName),
        CONCAT(d.firstName, ' ', d.lastName),
        m.name,
        pm.dosage,
        p.issuedDate
    )
    FROM Prescription p
    JOIN p.appointment a
    JOIN a.patient pat
    JOIN a.doctor d
    JOIN p.prescriptionMedications pm
    JOIN pm.medication m
    WHERE pat.patientId = :patientId
    ORDER BY p.issuedDate DESC
""")
    List<PrescriptionReportDTO> findReportsByPatientId(Long patientId);

    @Query("""
    SELECT new com.hospital.model.DTO.PrescriptionReportDTO(
        p.prescriptionId,
        a.appointmentId,
        CONCAT(pat.firstName, ' ', pat.lastName),
        CONCAT(d.firstName, ' ', d.lastName),
        m.name,
        pm.dosage,
        p.issuedDate
    )
    FROM Prescription p
    JOIN p.appointment a
    JOIN a.patient pat
    JOIN a.doctor d
    JOIN p.prescriptionMedications pm
    JOIN pm.medication m
    WHERE d.doctorId = :doctorId
    ORDER BY p.issuedDate DESC
""")
    List<PrescriptionReportDTO> findReportsByDoctorId(Long doctorId);

}

