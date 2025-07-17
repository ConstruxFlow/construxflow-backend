package com.example.construxflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.construxflow.dto.MaterialRequestCreateDTO;
import com.example.construxflow.dto.MaterialRequestResponseDTO;
import com.example.construxflow.entity.Material_request;
import com.example.construxflow.service.MaterialRequestService;

@RestController
@RequestMapping("/api/material-requests")
@CrossOrigin(origins = "http://localhost:3000")
public class MaterialRequestController {

    @Autowired
    private MaterialRequestService materialRequestService;

    @PostMapping("/create")
    public ResponseEntity<?> createMaterialRequest(@RequestBody MaterialRequestCreateDTO dto) {
        try {
            Material_request saved = materialRequestService.createMaterialRequest(dto);

            MaterialRequestResponseDTO response = new MaterialRequestResponseDTO();
            response.setRequestId(saved.getRequest_id());
            response.setStatus(saved.getStatus());
            response.setAdditionalInfo(saved.getAdditional_info());
            response.setPriority(saved.getPriority());
            response.setRequestDate(saved.getRequest_date());
            response.setProjectName(dto.getProject());
            response.setPhaseName(dto.getPhase());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to create material request: " + e.getMessage());
        }
    }
} 