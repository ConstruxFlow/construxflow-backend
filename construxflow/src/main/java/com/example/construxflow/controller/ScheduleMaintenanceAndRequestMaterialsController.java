package com.example.construxflow.controller;

import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsDTO;
import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsResponseDTO;
import com.example.construxflow.service.ScheduleMaintenanceAndRequestMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule-maintenance-materials")
@CrossOrigin(origins = "http://localhost:")
public class ScheduleMaintenanceAndRequestMaterialsController {

    @Autowired
    private ScheduleMaintenanceAndRequestMaterialsService scheduleMaintenanceAndRequestMaterialsService;

    // Create equipment scheduling with material requests
    @PostMapping
    public ResponseEntity<ScheduleMaintenanceAndRequestMaterialsResponseDTO> createScheduleAndRequestMaterials(
            @RequestBody ScheduleMaintenanceAndRequestMaterialsDTO requestDTO) {
        try {
            ScheduleMaintenanceAndRequestMaterialsResponseDTO response =
                    scheduleMaintenanceAndRequestMaterialsService.createScheduleAndRequestMaterials(requestDTO);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            ScheduleMaintenanceAndRequestMaterialsResponseDTO errorResponse =
                    ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                            .message("Failed to create equipment scheduling and material requests: " + e.getMessage())
                            .success(false)
                            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Get equipment scheduling with material requests by equipment ID
    @GetMapping("/{equipmentId}")
    public ResponseEntity<ScheduleMaintenanceAndRequestMaterialsResponseDTO> getScheduleAndMaterialsByEquipmentId(
            @PathVariable String equipmentId) {
        try {
            ScheduleMaintenanceAndRequestMaterialsResponseDTO response =
                    scheduleMaintenanceAndRequestMaterialsService.getScheduleAndMaterialsByEquipmentId(equipmentId);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ScheduleMaintenanceAndRequestMaterialsResponseDTO errorResponse =
                    ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                            .message("Failed to retrieve data: " + e.getMessage())
                            .success(false)
                            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    // Update equipment scheduling with material requests
    @PutMapping("/{equipmentId}")
    public ResponseEntity<ScheduleMaintenanceAndRequestMaterialsResponseDTO> updateScheduleAndRequestMaterials(
            @PathVariable String equipmentId,
            @RequestBody ScheduleMaintenanceAndRequestMaterialsDTO requestDTO) {
        try {
            ScheduleMaintenanceAndRequestMaterialsResponseDTO response =
                    scheduleMaintenanceAndRequestMaterialsService.updateScheduleAndRequestMaterials(equipmentId, requestDTO);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            ScheduleMaintenanceAndRequestMaterialsResponseDTO errorResponse =
                    ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                            .message("Failed to update equipment scheduling and material requests: " + e.getMessage())
                            .success(false)
                            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Delete equipment scheduling with material requests
    @DeleteMapping("/{equipmentId}")
    public ResponseEntity<ScheduleMaintenanceAndRequestMaterialsResponseDTO> deleteScheduleAndRequestMaterials(
            @PathVariable String equipmentId) {
        try {
            scheduleMaintenanceAndRequestMaterialsService.deleteScheduleAndRequestMaterials(equipmentId);

            ScheduleMaintenanceAndRequestMaterialsResponseDTO response =
                    ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                            .message("Equipment scheduling and material requests deleted successfully")
                            .success(true)
                            .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ScheduleMaintenanceAndRequestMaterialsResponseDTO errorResponse =
                    ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                            .message("Failed to delete equipment scheduling and material requests: " + e.getMessage())
                            .success(false)
                            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    // Check if equipment has pending material requests
    @GetMapping("/{equipmentId}/has-pending-requests")
    public ResponseEntity<Boolean> hasEquipmentPendingMaterialRequests(@PathVariable String equipmentId) {
        try {
            boolean hasPendingRequests =
                    scheduleMaintenanceAndRequestMaterialsService.hasEquipmentPendingMaterialRequests(equipmentId);
            return ResponseEntity.ok(hasPendingRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }







    // Exception handler for general errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ScheduleMaintenanceAndRequestMaterialsResponseDTO> handleException(Exception e) {
        ScheduleMaintenanceAndRequestMaterialsResponseDTO errorResponse =
                ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                        .message("An unexpected error occurred: " + e.getMessage())
                        .success(false)
                        .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
