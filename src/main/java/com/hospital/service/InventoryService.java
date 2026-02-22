package com.hospital.service;

import com.hospital.model.DTO.InventoryViewDTO;
import com.hospital.model.Inventory;
import com.hospital.model.Medication;
import com.hospital.repository.InventoryRepository;
import com.hospital.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MedicationRepository medicationRepository;
    private static final int LOW_STOCK = 10;

    @CacheEvict(value = "inventory", key = "#medicationId")
    public void addStock(Long medicationId, int quantity) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        Inventory inventory = new Inventory();
        inventory.setMedication(medication);
        inventory.setQuantity(quantity);
        inventory.setLastUpdated(LocalDateTime.now());

        inventoryRepository.save(inventory);
    }

    @CacheEvict(value = "inventory", key = "#medicationId")
    public void updateStock(Long medicationId, int quantity) {
        Inventory inventory = inventoryRepository.findByMedicationMedicationId(medicationId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setQuantity(quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        // no save() needed, JPA dirty checking persists changes
    }

    @CacheEvict(value = "inventory", key = "#medicationId")
    public synchronized boolean deductStock(Long medicationId, int amount) {
        Inventory inventory = inventoryRepository.findByMedicationMedicationId(medicationId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getQuantity() < amount)
            return false;

        inventory.setQuantity(inventory.getQuantity() - amount);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory); // Manually saving to ensure immediate flush within synchronized block
        return true;
    }

    @Cacheable(value = "inventory", key = "#medicationId")
    @Transactional(readOnly = true)
    public int getStock(Long medicationId) {
        return inventoryRepository.findByMedicationMedicationId(medicationId)
                .map(Inventory::getQuantity)
                .orElse(0);
    }

    @Cacheable("inventoryView")
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<InventoryViewDTO> getInventoryView(int page, int size) {
        return inventoryRepository
                .findInventoryWithMedication(org.springframework.data.domain.PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public List<InventoryViewDTO> getLowStockItems() {
        return inventoryRepository.findLowStockItems(LOW_STOCK);
    }

    @Transactional(readOnly = true)
    public Page<InventoryViewDTO> getInventoryPage(int page, int size) {
        return inventoryRepository.findInventoryWithMedication(PageRequest.of(page, size));
    }
}
