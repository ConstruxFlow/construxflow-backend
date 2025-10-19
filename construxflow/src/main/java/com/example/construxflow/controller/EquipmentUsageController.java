package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentLastUsageDTO;
import com.example.construxflow.dto.EquipmentUsageResponseDTO;
import com.example.construxflow.dto.EquipmentUsageSummaryDTO;
import com.example.construxflow.dto.EquipmentUsageUpdateDTO;
import com.example.construxflow.service.EquipmentUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-usage")
@CrossOrigin("http://localhost:3000/")
public class EquipmentUsageController {

    @Autowired
    private EquipmentUsageService equipmentUsageService;

    @PostMapping
    public ResponseEntity<EquipmentUsageResponseDTO> updateEquipmentUsage(@RequestBody EquipmentUsageUpdateDTO usageUpdateDTO) {
        try {
            System.out.println("Received equipment usage update: " + usageUpdateDTO);
            EquipmentUsageResponseDTO response = equipmentUsageService.updateEquipmentUsage(usageUpdateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            System.err.println("Error updating equipment usage: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EquipmentUsageResponseDTO>> getAllEquipmentUsageDetails() {
        try {
            System.out.println("GET request received for all equipment usage details");
            List<EquipmentUsageResponseDTO> usageDetails = equipmentUsageService.getAllEquipmentUsageDetails();
            return ResponseEntity.ok(usageDetails);
        } catch (Exception e) {
            System.err.println("Error fetching equipment usage details: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/summary/{equipmentId}")
    public ResponseEntity<EquipmentUsageSummaryDTO> getEquipmentUsageSummary(@PathVariable Long equipmentId) {
        try {
            System.out.println("GET request received for equipment usage summary, Equipment ID: " + equipmentId);
            EquipmentUsageSummaryDTO summary = equipmentUsageService.getEquipmentUsageSummary(equipmentId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            System.err.println("Error fetching equipment usage summary: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/last-usage/{equipmentId}")
    public ResponseEntity<EquipmentLastUsageDTO> getEquipmentLastUsage(@PathVariable Long equipmentId) {
        try {
            System.out.println("GET request received for last usage details, Equipment ID: " + equipmentId);
            EquipmentLastUsageDTO lastUsage = equipmentUsageService.getEquipmentLastUsage(equipmentId);
            return ResponseEntity.ok(lastUsage);
        } catch (Exception e) {
            System.err.println("Error fetching equipment last usage: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
