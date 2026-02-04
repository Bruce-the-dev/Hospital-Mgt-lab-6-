package com.hospital.model.mapper;

import com.hospital.model.Appointment;
import com.hospital.model.DTO.AppointmentInput;
import com.hospital.model.DTO.AppointmentResponse;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;

public class AppointmentMapper {

    public static Appointment toEntity(AppointmentInput input, Patient patient, Doctor doctor) {
        Appointment a = new Appointment();
        a.setPatient(patient);
        a.setDoctor(doctor);
        a.setAppointmentDate(input.getAppointmentDate());
        a.setStatus(input.getStatus());
        return a;
    }

    public static void updateEntity(Appointment appointment, AppointmentInput input, Patient patient, Doctor doctor) {
        if (patient != null) {
            appointment.setPatient(patient);}

        if (doctor != null) {
            appointment.setDoctor(doctor);}

        if (input.getAppointmentDate() != null) {
            appointment.setAppointmentDate(input.getAppointmentDate());}

        if (input.getStatus() != null) {
            appointment.setStatus(input.getStatus());}
    }

    public static AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getStatus()
        );
    }
}

