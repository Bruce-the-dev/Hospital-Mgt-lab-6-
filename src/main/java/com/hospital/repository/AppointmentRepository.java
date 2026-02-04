package com.hospital.repository;

import com.hospital.model.Appointment;
import com.hospital.model.DTO.AppointmentReportDTO;
import com.hospital.model.DTO.AppointmentResponse;
import com.hospital.model.DTO.FullAppointmentReportDTO;
import com.hospital.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //  Appointments for a patient (show doctor name)
    @Query("SELECT new com.hospital.model.DTO.AppointmentReportDTO(" +
            "a.appointmentId, a.appointmentDate, a.status, concat(d.firstName, ' ', d.lastName)) " +
            "FROM Appointment a " +
            "JOIN a.doctor d " +
            "WHERE a.patient.patientId = :patientId")
    List<AppointmentReportDTO> findAppointmentsByPatient(@Param("patientId") Long patientId, Pageable pageable);

    //  Appointments for a doctor (show patient name)
    @Query("SELECT new com.hospital.model.DTO.AppointmentReportDTO(" +
            "a.appointmentId, a.appointmentDate, a.status, concat(p.firstName, ' ', p.lastName)) " +
            "FROM Appointment a " +
            "JOIN a.patient p " +
            "WHERE a.doctor.doctorId = :doctorId")
    List<AppointmentReportDTO> findAppointmentsByDoctor(@Param("doctorId") Long doctorId, Pageable pageable);

    //  Full appointment report (patient + doctor + department)
    @Query("SELECT new com.hospital.model.DTO.FullAppointmentReportDTO(" +
            "a.appointmentId, a.appointmentDate, a.status, " +
            "concat(p.firstName, ' ', p.lastName), " +
            "concat(d.firstName, ' ', d.lastName), dep.name) " +
            "FROM Appointment a " +
            "JOIN a.patient p " +
            "JOIN a.doctor d " +
            "JOIN d.department dep")
    List<FullAppointmentReportDTO> findFullAppointmentReport(Pageable pageable);

    //  Appointments without prescription
    @Query("SELECT new com.hospital.model.DTO.FullAppointmentReportDTO(" +
            "a.appointmentId, a.appointmentDate, a.status, " +
            "concat(p.firstName, ' ', p.lastName), " +
            "concat(d.firstName, ' ', d.lastName), dep.name) " +
            "FROM Appointment a " +
            "JOIN a.patient p " +
            "JOIN a.doctor d " +
            "JOIN d.department dep " +
            "WHERE NOT EXISTS (" +
            "  SELECT pr FROM Prescription pr WHERE pr.appointment = a" +
            ") " +
            "ORDER BY a.appointmentDate")
    List<FullAppointmentReportDTO> findAppointmentsWithoutPrescription();

    //  Optional: search by status
    List<Appointment> findByStatus(Status status);


}

