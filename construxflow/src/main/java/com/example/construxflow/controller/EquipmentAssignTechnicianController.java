package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentAssignTechnicianRequestDTO;
import com.example.construxflow.dto.EquipmentAssignTechnicianResponseDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.service.EquipmentAssignTechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/equipmentassigntechnician")
@CrossOrigin(origins = "http://localhost:3000")
public class EquipmentAssignTechnicianController {

    @Autowired
    private EquipmentAssignTechnicianService equipmentAssignTechnicianService;

    @PostMapping("/addassign")
    public ResponseEntity<?> addAssign(@RequestBody EquipmentAssignTechnicianRequestDTO requestDTO) {
        try {
            EquipmentAssignTechnicianResponseDTO responseDTO = equipmentAssignTechnicianService.assignTechnician(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            // Optionally log the exception here
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error assigning technician: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEquipmentAssignTechnician() {
        try {
            List<EquipmentAssignTechnicianResponseDTO> equipmentAssignList = equipmentAssignTechnicianService.getEquipmentAssignDetails();
            return ResponseEntity.ok(equipmentAssignList);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error Get Equipment Assign Details" + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<EquipmentAssignTechnicianResponseDTO> getEquipmentAssignDetailsById(@RequestParam String id) {
        Optional<EquipmentAssignTechnicianResponseDTO> equipmentResponseDto = equipmentAssignTechnicianService.getEquipmentSchedulingDetailsById(id);
        return equipmentResponseDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/status")
    public ResponseEntity<EquipmentAssignTechnicianResponseDTO> updateEquipmentAssignStatus(
            @RequestParam String id,
            @RequestBody String newStatus
    ) {
        EquipmentAssignTechnicianResponseDTO updated = equipmentAssignTechnicianService.updateAssignStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }

}
