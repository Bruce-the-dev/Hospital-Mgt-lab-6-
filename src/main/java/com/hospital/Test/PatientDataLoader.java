package com.hospital.Test;

import com.hospital.model.Patient;
import com.hospital.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Order(value = 2)
public class PatientDataLoader implements CommandLineRunner {

    private final PatientRepository patientRepository;

    @Override
    public void run(String... args) {

        if (patientRepository.count() == 0) {

            Patient p1 = new Patient(null,
                    "John",
                    "Doe",
                    LocalDate.of(1995, 5, 10),
                    'M',
                    "john@example.com",
                    "1234567890");

            Patient p2 = new Patient(null,
                    "Sarah",
                    "Smith",
                    LocalDate.of(2000, 3, 15),
                    'F',
                    "sarah@example.com",
                    "0987654321");

            patientRepository.save(p1);
            patientRepository.save(p2);

            System.out.println("Patients loaded!");
        }
    }
}
