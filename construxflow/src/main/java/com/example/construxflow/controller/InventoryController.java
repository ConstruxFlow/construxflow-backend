package com.example.construxflow.controller;

import com.example.construxflow.dto.InventoryUpdateDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.I_Material;
import com.example.construxflow.service.EquipmentService;
import com.example.construxflow.service.I_MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final EquipmentService equipmentService;
    private final I_MaterialService materialService;

    // Search inventory items by name and type
    // In InventoryController.java - make sure it looks like this:
    @GetMapping("/search")
    public ResponseEntity<?> searchInventory(
            @RequestParam String type,
            @RequestParam String name) {

        try {
            if ("equipment".equalsIgnoreCase(type)) {
                // ✅ This calls the service method directly, not the endpoint
                List<Equipment> equipment = equipmentService.searchEquipmentByName(name);
                return ResponseEntity.ok(equipment);
            } else if ("material".equalsIgnoreCase(type)) {
                List<I_Material> materials = materialService.searchMaterialsByName(name);
                return ResponseEntity.ok(materials);
            } else {
                return ResponseEntity.badRequest().body("Invalid type. Use 'equipment' or 'material'.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error searching inventory: " + e.getMessage());
        }
    }

    // Update inventory stock
    @PostMapping("/update-stock")
    public ResponseEntity<?> updateInventoryStock(@RequestBody InventoryUpdateDTO updateDTO) {
        try {
            if ("equipment".equalsIgnoreCase(updateDTO.getType())) {
                Equipment equipment = equipmentService.getEquipmentById(updateDTO.getId());
                if (equipment == null) {
                    return ResponseEntity.notFound().build();
                }

                Integer currentStock = equipment.getQuantity();
                Integer newStock = calculateNewStock(currentStock, updateDTO.getAction(), updateDTO.getQuantity());

                Equipment updated = equipmentService.updateEquipmentStock(updateDTO.getId(), newStock);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Equipment stock updated successfully");
                response.put("item", updated);
                response.put("newStock", newStock);

                return ResponseEntity.ok(response);

            } else if ("material".equalsIgnoreCase(updateDTO.getType())) {
                I_Material material = materialService.getMaterialById(updateDTO.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Material not found"));

                Integer currentStock = material.getQuantityInStock();
                Integer newStock = calculateNewStock(currentStock, updateDTO.getAction(), updateDTO.getQuantity());

                I_Material updated = materialService.updateMaterialStock(updateDTO.getId(), newStock);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Material stock updated successfully");
                response.put("item", updated);
                response.put("newStock", newStock);

                return ResponseEntity.ok(response);

            } else {
                return ResponseEntity.badRequest().body("Invalid type. Use 'equipment' or 'material'.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating inventory: " + e.getMessage());
        }
    }

    private Integer calculateNewStock(Integer currentStock, String action, Integer quantity) {
        if ("add".equalsIgnoreCase(action)) {
            return currentStock + quantity;
        } else if ("remove".equalsIgnoreCase(action)) {
            int newStock = currentStock - quantity;
            if (newStock < 0) {
                throw new IllegalArgumentException("Insufficient stock. Current: " + currentStock);
            }
            return newStock;
        } else {
            throw new IllegalArgumentException("Invalid action. Use 'add' or 'remove'.");
        }
    }
}