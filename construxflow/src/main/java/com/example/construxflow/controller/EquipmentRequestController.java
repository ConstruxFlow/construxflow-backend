package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentRequestDTO;
import com.example.construxflow.dto.EquipmentRequestResponseDTO;
import com.example.construxflow.service.EquipmentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-requests")
@CrossOrigin("http://localhost:3000")
public class EquipmentRequestController {

    @Autowired
    private EquipmentRequestService equipmentRequestService;

    @PostMapping
    public ResponseEntity<EquipmentRequestResponseDTO> createEquipmentRequest(@RequestBody EquipmentRequestDTO requestDTO) {
        try {
            EquipmentRequestResponseDTO responseDTO = equipmentRequestService.createEquipmentRequest(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<EquipmentRequestResponseDTO>> getAllEquipmentRequests() {
        try{
            List<EquipmentRequestResponseDTO> requests = equipmentRequestService.getAllEquipmentRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<EquipmentRequestResponseDTO> updateEquipmentRequestStatus(
            @RequestParam Long requestId,
            @RequestParam String status) {
        try {
            EquipmentRequestResponseDTO updatedRequest = equipmentRequestService.updateEquipmentRequestStatus(requestId, status);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
