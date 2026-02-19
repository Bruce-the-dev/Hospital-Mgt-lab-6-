package com.hospital.Test;

import com.hospital.model.Department;
import com.hospital.model.Doctor;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(value = 3)
public class DoctorDataLoader implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) {

        if (doctorRepository.count() == 0) {


            if (doctorRepository.count() == 0) {

                Department cardiology = departmentRepository.findAll().getFirst();

                Doctor doctor1 = new Doctor();
                doctor1.setFirstName("Alice");
                doctor1.setLastName("Brown");
                doctor1.setSpecialization("Heart Specialist");
                doctor1.setDepartment(cardiology);  // ✅ correct

                doctorRepository.save(doctor1);

                System.out.println("Doctors loaded!");

                cardiology = departmentRepository.findAll().get(1);

                Doctor doctor2 = new Doctor();
                doctor2.setFirstName("Alice");
                doctor2.setLastName("Brown");
                doctor2.setSpecialization("Heart Specialist");
                doctor2.setDepartment(cardiology);  // ✅ correct

                doctorRepository.save(doctor2);

                System.out.println("Doctors loaded!");
                doctorRepository.save(doctor2);

                System.out.println("Doctors loaded!");
            }
        }
    }
}
