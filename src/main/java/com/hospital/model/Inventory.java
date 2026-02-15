package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    private int quantity;

    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "medication_id", nullable = false, unique = true)
    private Medication medication;

    @PrePersist
    public void prePersist() {
        this.lastUpdated = LocalDateTime.now();
    }
}
