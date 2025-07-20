package com.example.construxflow.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.construxflow.api_response.ApiResponse;
import com.example.construxflow.dto.SupplierRegReqDTO;
import com.example.construxflow.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.construxflow.service.SupplierService;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin(origins = "http://localhost:3000")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/latest-id")
    public ResponseEntity<Map<String, Object>> getLatestSupplierId() {
        try {
            String latestSupplierId = supplierService.getLatestSupplierId();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", latestSupplierId);
            response.put("message", "Supplier ID retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error fetching latest supplier ID: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/Register")
    public  ResponseEntity<ApiResponse<String>> register(@RequestBody SupplierRegReqDTO supplier) {
        try{
            String message=supplierService.registerSuppler(supplier);
            ApiResponse<String> response = ApiResponse.success("Supplier ID retrieved successfully", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = ApiResponse.error("Error fetching latest supplier ID: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }

    }

}
