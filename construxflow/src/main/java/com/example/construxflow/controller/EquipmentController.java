package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping("/add")
    public Equipment addEquipment(@RequestBody EquipmentDTO dto) {
        return equipmentService.addEquipment(dto);
    }
}
