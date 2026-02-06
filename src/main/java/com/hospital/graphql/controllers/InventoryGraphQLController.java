package com.hospital.graphql.controllers;

import com.hospital.model.DTO.InventoryViewDTO;
import com.hospital.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InventoryGraphQLController {

    private final InventoryService inventoryService;

    public InventoryGraphQLController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @QueryMapping
    public Page<InventoryViewDTO> inventoryViewPage(@Argument int page, @Argument int size) {
        return inventoryService.getInventoryPage(page, size);
    }

    @QueryMapping
    public List<InventoryViewDTO> lowStockItems() {
        return inventoryService.getLowStockItems();
    }

    @QueryMapping
    public List<InventoryViewDTO> inventoryView() {
        return inventoryService.getInventoryView();
    }
}
