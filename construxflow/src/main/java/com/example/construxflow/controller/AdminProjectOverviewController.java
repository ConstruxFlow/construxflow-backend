package com.example.construxflow.controller;

import com.example.construxflow.dto.AdminProjectOverviewDTO;
import com.example.construxflow.service.AdminProjectOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class AdminProjectOverviewController {

    private final AdminProjectOverviewService service;

    @GetMapping("/overview")
    public List<AdminProjectOverviewDTO> getProjectsOverview() {
        return service.getOverview();
    }
}