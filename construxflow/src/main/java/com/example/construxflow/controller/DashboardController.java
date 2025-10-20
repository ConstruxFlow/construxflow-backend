package com.example.construxflow.controller;

import com.example.construxflow.dto.DashboardStatsDTO;
import com.example.construxflow.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        try {
            DashboardStatsDTO stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            // Return default values in case of error
            DashboardStatsDTO defaultStats = new DashboardStatsDTO(
                    0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L
            );
            return ResponseEntity.ok(defaultStats);
        }
    }
}