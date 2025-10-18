package com.example.construxflow.controller;

import com.example.construxflow.dto.I_MaterialDTO;
import com.example.construxflow.entity.I_Material;
import com.example.construxflow.service.I_MaterialService;
import com.example.construxflow.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/materials")
public class I_MaterialController {

    @Autowired
    private I_MaterialService materialService;

    @PostMapping("/add")
    public I_Material addMaterial(@RequestBody I_MaterialDTO dto) {
        return materialService.addMaterial(dto);
    }


    @GetMapping("/all")
    public List<I_Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<I_Material> getMaterialById(@PathVariable Long id) {
        Optional<I_Material> material = materialService.getMaterialById(id);
        return material.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Search materials by name
    @GetMapping("/search")
    public ResponseEntity<List<I_Material>> searchMaterials(@RequestParam String name) {
        List<I_Material> materials = materialService.searchMaterialsByName(name);
        return ResponseEntity.ok(materials);
    }

    // Update material stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<I_Material> updateMaterialStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        try {
            I_Material material = materialService.updateMaterialStock(id, quantity);
            return ResponseEntity.ok(material);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update full material
    @PutMapping("/{id}")
    public ResponseEntity<I_Material> updateMaterial(
            @PathVariable Long id,
            @RequestBody I_MaterialDTO dto) {
        try {
            I_Material material = materialService.updateMaterial(id, dto);
            return ResponseEntity.ok(material);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get low stock materials
    @GetMapping("/low-stock")
    public ResponseEntity<List<I_Material>> getLowStockMaterials() {
        List<I_Material> materials = materialService.getLowStockMaterials();
        return ResponseEntity.ok(materials);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMaterial(@PathVariable Long id) {
        try {
            boolean isDeleted = materialService.deleteMaterial(id);
            if (isDeleted) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Material deleted successfully");
                response.put("deletedId", id.toString());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to delete material"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    }
