package com.example.construxflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.construxflow.api_response.ApiResponse;
import com.example.construxflow.dto.SupplierDetailsDTO;
import com.example.construxflow.dto.SupplierRegReqDTO;
import com.example.construxflow.dto.SupplierRegResDTO;
import com.example.construxflow.entity.Supplier;
import com.google.api.gax.rpc.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.construxflow.service.SupplierService;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin(origins = "http://localhost:")
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
            if ("No suppliers found".equals(e.getMessage())) {
                errorResponse.put("message", "No suppliers found");
                return ResponseEntity.status(404).body(errorResponse); // 404 Not Found
            } else {
                errorResponse.put("message", "Error fetching latest supplier ID: " + e.getMessage());
                return ResponseEntity.status(500).body(errorResponse); // 500 Internal Server Error
            }
        }
    }


    @PostMapping("/Register")
    public  ResponseEntity<ApiResponse<?>> register(@RequestBody SupplierRegReqDTO supplier) {
        try{
            SupplierRegResDTO supplierData=supplierService.registerSuppler(supplier);
            ApiResponse<SupplierRegResDTO> response = ApiResponse.success("Supplier ID retrieved successfully", supplierData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = ApiResponse.error("Error fetching latest supplier ID: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllSuppliers() {
        try{
            List<SupplierDetailsDTO> suppliersData =supplierService.getAllSupplierDetails();
            ApiResponse<List<SupplierDetailsDTO>> response = ApiResponse.success(suppliersData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = ApiResponse.error("Error fetching all Suppliers " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ApiResponse<?>> getSupplier(@PathVariable("id") String id) {
        try{
            Supplier supplierData=supplierService.getSupplierDetails(id);
            ApiResponse<Supplier> response = ApiResponse.success(supplierData);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ApiResponse<String> errorResponse = ApiResponse.error("Supplier not found: " + e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> errorResponse = ApiResponse.error("Invalid supplier ID: " + e.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = ApiResponse.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateSupplier(
            @PathVariable("id") String id,
            @RequestBody Supplier updatedSupplier) {
        try {
            Supplier savedSupplier = supplierService.updateSupplier(id, updatedSupplier);
            ApiResponse response = ApiResponse.success("Supplier updated successfully", savedSupplier);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ApiResponse errorResponse = ApiResponse.error("Supplier not found: " + e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        } catch (Exception e) {
            ApiResponse errorResponse = ApiResponse.error("Error updating supplier: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }


}
