// src/main/java/com/example/construxflow/controller/AdminDashboardSummaryController.java
package com.example.construxflow.controller;

import com.example.construxflow.dto.AdminDashboardSummaryDTO;
import com.example.construxflow.service.AdminDashboardSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")

@RequiredArgsConstructor
public class AdminDashboardSummaryController {

    private final AdminDashboardSummaryService dashboardService;

    @GetMapping("/summary")
    public AdminDashboardSummaryDTO getSummary() {
        return dashboardService.getSummary();
    }
}