package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors", indexes = {
        @Index(name = "idx_doctor_department", columnList = "department_id")
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(length = 100)
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}

