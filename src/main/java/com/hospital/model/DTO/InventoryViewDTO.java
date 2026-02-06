package com.hospital.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class InventoryViewDTO {
    private Long medicationId;
    private String medicationName;
    private int quantity;
    private LocalDateTime lastUpdated;

}
