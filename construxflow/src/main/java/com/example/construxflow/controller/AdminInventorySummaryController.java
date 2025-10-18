// src/main/java/com/example/construxflow/controller/InventorySummaryController.java
package com.example.construxflow.controller;

import com.example.construxflow.dto.AdminInventorySummaryDTO;
import com.example.construxflow.service.AdminInventorySummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class AdminInventorySummaryController {

    private final AdminInventorySummaryService service;

    @GetMapping("/summary")
    public AdminInventorySummaryDTO getSummary() {
        return service.getSummary();
    }
}