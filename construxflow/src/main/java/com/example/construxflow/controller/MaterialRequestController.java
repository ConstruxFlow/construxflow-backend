package com.example.construxflow.controller;

import com.example.construxflow.dto.MaterialRequestDetailDTO;
import com.example.construxflow.dto.RequestedMaterialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.construxflow.dto.MaterialRequestCreateDTO;
import com.example.construxflow.dto.MaterialRequestResponseDTO;
import com.example.construxflow.entity.Material_request;
import com.example.construxflow.service.MaterialRequestService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/all")
    public ResponseEntity<?> getAllMaterialRequests() {
        try {
            List<Material_request> requests = materialRequestService.getAllMaterialRequests();

            List<MaterialRequestDetailDTO> response = requests.stream()
                    .map(this::convertToDetailDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to fetch material requests: " + e.getMessage());
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getMaterialRequestById(@PathVariable Long id) {
        try {
            Optional<Material_request> request = materialRequestService.getMaterialRequestById(id);

            if (request.isPresent()) {
                MaterialRequestDetailDTO response = convertToDetailDTO(request.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to fetch material request: " + e.getMessage());
        }
    }

    private MaterialRequestDetailDTO convertToDetailDTO(Material_request request) {
        MaterialRequestDetailDTO dto = new MaterialRequestDetailDTO();
        dto.setRequestId(request.getRequest_id());
        dto.setStatus(request.getStatus());
        dto.setAdditionalInfo(request.getAdditional_info());
        dto.setPriority(request.getPriority());
        dto.setRequestDate(request.getRequest_date());
        dto.setProjectName(request.getProject_name());
        dto.setPhaseName(request.getPhase_name());

        // Set project ID if project exists
        if (request.getProject() != null) {
            dto.setProjectId(request.getProject().getProjectId());
        }

        // Convert requested materials
        if (request.getRequested_materials() != null) {
            List<RequestedMaterialDTO> requestedMaterials = request.getRequested_materials().stream()
                    .map(reqMat -> {
                        RequestedMaterialDTO reqMatDTO = new RequestedMaterialDTO();
                        reqMatDTO.setRequestedMaterialId(reqMat.getRequested_material_id());
                        reqMatDTO.setQuantity(reqMat.getQuantity());
                        reqMatDTO.setStatus(reqMat.getStatus());

                        if (reqMat.getMaterial() != null) {
                            reqMatDTO.setMaterialName(reqMat.getMaterial().getMaterialName());
                            reqMatDTO.setMaterialType(reqMat.getMaterial().getMaterialType());
                            reqMatDTO.setUnitOfMeasurement(reqMat.getMaterial().getUnitOfMeasurement());
                        }

                        return reqMatDTO;
                    })
                    .collect(Collectors.toList());

            dto.setRequestedMaterials(requestedMaterials);
        }

        return dto;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateMaterialRequestStatusParam(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            System.out.printf("\n",status,"\n");
            Material_request updatedRequest = materialRequestService.updateMaterialRequestStatus(id, status);
            MaterialRequestDetailDTO response = convertToDetailDTO(updatedRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            e.printStackTrace();
            return ResponseEntity.status(400).body("Failed to update material request status: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to update material request status: " + e.getMessage());
        }
    }
} 