package com.hospital.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

@Id
@GeneratedValue
private Long feedbackId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    private int rating;
    private String comment;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        lastUpdated = createdDate;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
