package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients"
//        ,indexes = {
//        @Index(name = "idx_last_name", columnList = "lastName")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false, length = 1)
    private char gender;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;
}
