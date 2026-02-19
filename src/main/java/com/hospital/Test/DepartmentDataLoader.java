package com.hospital.Test;

import com.hospital.model.Department;
import com.hospital.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class DepartmentDataLoader implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) {

        if (departmentRepository.count() == 0) {

            Department d1 = new Department(null, "Kine therapy","KGL");
            Department d2 = new Department(null, "Neurology","North");

            departmentRepository.save(d1);
            departmentRepository.save(d2);

            System.out.println("Departments loaded!");
        }
    }
}
