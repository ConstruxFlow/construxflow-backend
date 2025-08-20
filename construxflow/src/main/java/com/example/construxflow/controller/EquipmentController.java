package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentDTO;
import com.example.construxflow.dto.NextEquipmentScheduleResponseDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.service.EquipmentService;
import com.example.construxflow.service.NextEquipmentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private NextEquipmentScheduleService nextEquipmentScheduleService;

    @PostMapping("/add")
    public Equipment addEquipment(@RequestBody EquipmentDTO dto) {
        return equipmentService.addEquipment(dto);
    }

    @GetMapping("/all")
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return equipment != null ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-id")
    public ResponseEntity<Equipment> getEquipmentByIdParam(@RequestParam Long id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return equipment != null ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/scheduled-equipment-details")
    public ResponseEntity<List<Equipment>> getScheduledEquipmentDetails() {
        // Get unique equipment IDs from next schedules
        List<NextEquipmentScheduleResponseDTO> schedules = nextEquipmentScheduleService.getAllNextScheduleDetails();

        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Extract equipment IDs and get their full details
        List<Equipment> equipmentDetails = schedules.stream()
                .map(schedule -> {
                    try {
                        Long equipmentId = Long.parseLong(schedule.getEquipmentId());
                        return equipmentService.getEquipmentById(equipmentId);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(equipment -> equipment != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(equipmentDetails);
    }
}