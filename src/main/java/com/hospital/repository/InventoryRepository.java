package com.hospital.repository;

import com.hospital.model.DTO.InventoryViewDTO;
import com.hospital.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByMedicationMedicationId(Long medicationId);

    @Query("""
                SELECT new com.hospital.model.DTO.InventoryViewDTO(
                    i.medication.medicationId,
                    m.name,
                    i.quantity,
                    i.lastUpdated
                )
                FROM Inventory i
                JOIN i.medication m
                ORDER BY m.name
            """)
    List<InventoryViewDTO> findInventoryWithMedication();

    @Query("""
                SELECT new com.hospital.model.DTO.InventoryViewDTO(
                    i.medication.medicationId,
                    m.name,
                    i.quantity,
                    i.lastUpdated
                )
                FROM Inventory i
                JOIN i.medication m
            """)
    Page<InventoryViewDTO> findInventoryWithMedication(Pageable pageable);

    @Query("""
                SELECT new com.hospital.model.DTO.InventoryViewDTO(
                    i.medication.medicationId,
                    m.name,
                    i.quantity,
                    i.lastUpdated
                )
                FROM Inventory i
                JOIN i.medication m
                WHERE i.quantity <= :lowStockLimit
            """)
    List<InventoryViewDTO> findLowStockItems(int lowStockLimit);
}
