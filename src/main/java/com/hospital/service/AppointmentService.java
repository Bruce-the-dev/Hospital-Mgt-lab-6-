package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.Appointment;
import com.hospital.model.DTO.AppointmentInput;
import com.hospital.model.DTO.AppointmentReportDTO;
import com.hospital.model.DTO.AppointmentResponse;
import com.hospital.model.DTO.FullAppointmentReportDTO;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.model.Status;
import com.hospital.model.mapper.AppointmentMapper;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @CachePut(value = "appointments", key = "#result.appointmentId")
    public AppointmentResponse addAppointment(AppointmentInput input) {
        Patient patient = patientRepository.findById(input.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(input.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Appointment appointment = AppointmentMapper.toEntity(input, patient, doctor);
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentMapper.toResponse(saved);
    }


    @Cacheable(value = "appointments", key = "#id")
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return AppointmentMapper.toResponse(appointment);
    }


    @Transactional(readOnly = true)
    public List<AppointmentResponse> searchByStatus(Status status) {
        return appointmentRepository.findByStatus(status).stream().map(AppointmentMapper::toResponse).toList();
    }

    @CacheEvict(value = "appointments", allEntries = true)
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found");
        }
        appointmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> sortByDate() {
        List<Appointment> list = appointmentRepository.findAll();
        list.sort(Comparator.comparing(Appointment::getAppointmentDate));
        return list.stream().map(AppointmentMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "appointmentReports", key = "'withoutPrescription'")
    public List<FullAppointmentReportDTO> getAppointmentsWithoutPrescription() {
        return appointmentRepository.findAppointmentsWithoutPrescription();
    }
    @Transactional(readOnly = true)
    @Cacheable(value = "appointmentReports", key = "'doctor_'+#doctorId")
    public List<AppointmentReportDTO> getAppointmentsByDoctor(Long doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("AppointmentDate").descending());
        return appointmentRepository.findAppointmentsByDoctor(doctorId, pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "appointmentReports", key = "'fullReport'")
    public List<FullAppointmentReportDTO> getFullAppointmentReport(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").descending());
        return appointmentRepository.findFullAppointmentReport(pageable);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "appointmentReports", key = "'patient_'+#patientId")
    public List<AppointmentReportDTO> getAppointmentsByPatient(Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").descending());
        return appointmentRepository.findAppointmentsByPatient(patientId,pageable);
    }
    @CachePut(value = "appointments", key = "#id")
    public AppointmentResponse updateAppointment(Long id, AppointmentInput input) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Patient patient = input.getPatientId() == null ? null :
                patientRepository.findById(input.getPatientId())
                        .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = input.getDoctorId() == null ? null :
                doctorRepository.findById(input.getDoctorId())
                        .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        AppointmentMapper.updateEntity(appointment, input, patient, doctor);

        return AppointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

}

