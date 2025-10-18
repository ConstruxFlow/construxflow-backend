package com.example.construxflow.controller;

import com.example.construxflow.dto.InventoryRequestCreateDTO;
import com.example.construxflow.dto.InventoryRequestResponseDTO;
import com.example.construxflow.service.InventoryRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/inventory/requests")
public class InventoryRequestController {

    private final InventoryRequestService service;

    public InventoryRequestController(InventoryRequestService service) {
        this.service = service;
    }

    // Create a new inventory request
    @PostMapping
    public InventoryRequestResponseDTO create(@RequestBody InventoryRequestCreateDTO dto) {
        return service.create(dto);
    }

    // Get all requests
    @GetMapping
    public List<InventoryRequestResponseDTO> getAll() {
        return service.findAll();
    }

    // Get one by id
    @GetMapping("/{id}")
    public InventoryRequestResponseDTO getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    // Approve request
    @PostMapping("/{id}/approve")
    public InventoryRequestResponseDTO approve(@PathVariable Long id) {
        return service.approve(id);
    }

    // Reject request
    @PostMapping("/{id}/reject")
    public InventoryRequestResponseDTO reject(@PathVariable Long id) {
        return service.reject(id);
    }

    // Mark partial (optional)
    @PostMapping("/{id}/partial")
    public InventoryRequestResponseDTO partial(@PathVariable Long id) {
        return service.markPartial(id);
    }
}
