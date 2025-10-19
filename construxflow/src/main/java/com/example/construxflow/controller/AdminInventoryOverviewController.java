package com.example.construxflow.controller;

import com.example.construxflow.dto.AdminInventoryOverviewDTO;
import com.example.construxflow.dto.AdminInventoryOverviewDTO.CriticalItemDTO;
import com.example.construxflow.entity.I_Material;
import com.example.construxflow.service.AdminInventoryOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryOverviewController {

    private final AdminInventoryOverviewService service;

    // Main overview for dashboard cards and critical list
    @GetMapping("/overview")
    public AdminInventoryOverviewDTO getOverview() {
        return service.getOverview();
    }

    // Critical items only
    @GetMapping("/critical")
    public List<CriticalItemDTO> getCritical() {
        return service.getCriticalItems();
    }

    // Paginated materials list
    @GetMapping("/materials")
    public Page<I_Material> listMaterials(
            Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        return service.getMaterials(pageable, category);
    }
}