package com.example.construxflow.controller;

import com.example.construxflow.dto.AdminEquipmentSummaryDTO;
import com.example.construxflow.service.AdminEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // adjust as needed for your frontend host
public class AdminEquipmentController {

    private final AdminEquipmentService equipmentService;

    @GetMapping("/summary")
    public List<AdminEquipmentSummaryDTO> listSummaries() {
        return equipmentService.getEquipmentSummaries();
    }
}