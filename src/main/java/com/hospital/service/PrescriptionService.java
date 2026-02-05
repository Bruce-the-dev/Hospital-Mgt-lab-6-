package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.Appointment;
import com.hospital.model.DTO.PrescriptionCreateRequest;
import com.hospital.model.DTO.PrescriptionMedicationRequest;
import com.hospital.model.DTO.PrescriptionReportDTO;
import com.hospital.model.DTO.PrescriptionUpdateRequest;
import com.hospital.model.Medication;
import com.hospital.model.Prescription;
import com.hospital.model.PrescriptionMedication;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.MedicationRepository;
import com.hospital.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationRepository medicationRepository;

    @CacheEvict(value = "prescriptions", allEntries = true)
    public Prescription createPrescription(
            PrescriptionCreateRequest request
    ) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found with id " + request.getAppointmentId()));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setIssuedDate(request.getIssuedDate());
        prescription.setNotes(request.getNotes());

        List<PrescriptionMedication> meds = new ArrayList<>();


        // bind children
        for (PrescriptionMedicationRequest dto : request.getMedications()) {
            Medication medication = medicationRepository.findById(dto.getMedicationId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Medication not found with id " + dto.getMedicationId()));

            PrescriptionMedication pm = new PrescriptionMedication();
            pm.setDosage(dto.getDosage());
            pm.setMedication(medication);
            pm.setPrescription(prescription);

            meds.add(pm);
        }

        prescription.setPrescriptionMedications(meds);
        return prescriptionRepository.save(prescription);
    }

    @Cacheable(value = "prescriptions", key = "'id:' + #id")
    @Transactional(readOnly = true)
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prescription not found with id " + id));
    }

    @CacheEvict(value = "prescriptions", allEntries = true)
    public Prescription updatePrescription(
            Long prescriptionId,
            PrescriptionUpdateRequest request
    ) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prescription not found with id " + prescriptionId));

        if (request.getIssuedDate() != null) {
            prescription.setIssuedDate(request.getIssuedDate());
        }

        if (request.getNotes() != null) {
            prescription.setNotes(request.getNotes());
        }

        if (request.getMedications() != null) {
            prescription.getPrescriptionMedications().clear();

            for (PrescriptionMedicationRequest mr : request.getMedications()) {
                PrescriptionMedication pm = new PrescriptionMedication();
                pm.setDosage(mr.getDosage());
                pm.setMedication(
                        medicationRepository.findById(mr.getMedicationId())
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Medication not found with id " + mr.getMedicationId()))
                );
                pm.setPrescription(prescription);
                prescription.getPrescriptionMedications().add(pm);
            }
        }

        return prescription;
    }

    @CacheEvict(value = "prescriptions", allEntries = true)
    public void deletePrescription(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prescription not found with id " + prescriptionId));

        prescriptionRepository.delete(prescription);
    }

    @Transactional(readOnly = true)
    public List<PrescriptionReportDTO> getAllPrescriptionReports(int size, int page, String sortBy, String direction) {

        Pageable pageable = PageRequest.of(page, size, direction.equalsIgnoreCase("desc")? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return prescriptionRepository.findAllPrescriptionReports(pageable);
    }

    @Transactional(readOnly = true)
    public List<PrescriptionReportDTO> getReportsByPatient(Long patientId) {
        return prescriptionRepository.findReportsByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<PrescriptionReportDTO> getReportsByDoctor(Long doctorId) {
        return prescriptionRepository.findReportsByDoctorId(doctorId);
    }


}
