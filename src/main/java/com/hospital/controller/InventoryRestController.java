package com.hospital.controller;

import com.hospital.model.DTO.InventoryViewDTO;
import com.hospital.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "Endpoints for managing hospital inventory")
@RequiredArgsConstructor
public class InventoryRestController {

    private final InventoryService inventoryService;


    @GetMapping("/all")
    @Operation(summary = "Get all inventory items")
    public ResponseEntity<List<InventoryViewDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getInventoryView());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get inventory items with low stock")
    public ResponseEntity<List<InventoryViewDTO>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/{medicationId}/stock")
    @Operation(summary = "Get current stock for a specific medication")
    public ResponseEntity<Integer> getStock(
            @Parameter(description = "Medication ID") @PathVariable Long medicationId) {
        return ResponseEntity.ok(inventoryService.getStock(medicationId));
    }

    @GetMapping("/page")
    @Operation(summary = "Get inventory with pagination")
    public ResponseEntity<Page<InventoryViewDTO>> getInventoryPage(
            @Parameter(description = "Page number (0-based)") @RequestParam int page,
            @Parameter(description = "Page size") @RequestParam int size) {
        return ResponseEntity.ok(inventoryService.getInventoryPage(page, size));
    }


    @PostMapping("/{medicationId}/add")
    @Operation(summary = "Add stock for a medication")
    public ResponseEntity<Void> addStock(
            @Parameter(description = "Medication ID") @PathVariable Long medicationId,
            @Parameter(description = "Quantity to add") @RequestParam int quantity) {
                inventoryService.addStock(medicationId, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{medicationId}/update")
    @Operation(summary = "Update stock for a medication")
    public ResponseEntity<Void> updateStock(
            @Parameter(description = "Medication ID") @PathVariable Long medicationId,
            @Parameter(description = "New quantity") @RequestParam int quantity) {
                inventoryService.updateStock(medicationId, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{medicationId}/deduct")
    @Operation(summary = "Deduct stock for a medication")
    public ResponseEntity<Boolean> deductStock(
            @Parameter(description = "Medication ID") @PathVariable Long medicationId,
            @Parameter(description = "Quantity to deduct") @RequestParam int amount) {
        return ResponseEntity.ok(inventoryService.deductStock(medicationId, amount));
    }
}
