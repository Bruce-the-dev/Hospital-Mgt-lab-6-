package com.hospital.Test;

import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.model.Status;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Order(value = 4)
public class AppointmentDataLoader implements CommandLineRunner {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public void run(String... args) {
        if (appointmentRepository.count() == 0) {
        Patient p = patientRepository.findAll().getFirst();
        Doctor d = doctorRepository.findAll().getFirst();

        for (int i = 0; i < 5000; i++) {
            Appointment a = new Appointment();
            a.setPatient(p);
            a.setDoctor(d);
            a.setStatus(Status.PENDING);
            a.setAppointmentDate(LocalDateTime.now().minusDays(i));
            appointmentRepository.save(a);
        }

        System.out.println("Inserted 5000 appointments");
    }
    }
}
