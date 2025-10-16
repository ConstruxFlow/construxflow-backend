package com.example.construxflow.controller;

import com.example.construxflow.dto.AdminDisplayDTO;
import com.example.construxflow.service.AdminDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "*")
public class AdminDisplayController {

    private final AdminDisplayService adminDisplayService;

    @Autowired
    public AdminDisplayController(AdminDisplayService adminDisplayService) {
        this.adminDisplayService = adminDisplayService;
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<AdminDisplayDTO> getDashboardStats() {
        return ResponseEntity.ok(adminDisplayService.getDashboardStats());
    }
}