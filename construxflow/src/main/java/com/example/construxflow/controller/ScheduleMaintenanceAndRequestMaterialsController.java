package com.example.construxflow.controller;
import com.example.construxflow.dto.MaintenanceRequestOverviewDTO;
import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsDTO;
import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsResponseDTO;
import com.example.construxflow.entity.Equipment_scheduling;
import com.example.construxflow.repository.EquipmentSchedulingRepository;
import com.example.construxflow.service.ScheduleMaintenanceAndRequestMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.construxflow.dto.MaintenanceRequestDetailDTO;

import java.util.List;
import java.util.Map;

import com.example.construxflow.dto.MaintenanceRequestStatusUpdateDTO;
import com.example.construxflow.dto.MaintenanceRequestActionResponseDTO;

@RestController
@RequestMapping("/api/schedule-maintenance-materials")
@CrossOrigin(origins = "http://localhost:")
public class ScheduleMaintenanceAndRequestMaterialsController {

    @Autowired
    private ScheduleMaintenanceAndRequestMaterialsService scheduleMaintenanceAndRequestMaterialsService;

    @Autowired // Add this autowired annotation
    private EquipmentSchedulingRepository equipmentSchedulingRepository;

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

    @GetMapping("/overview")
    public ResponseEntity<List<MaintenanceRequestOverviewDTO>> getAllMaintenanceRequestsOverview() {
        try {
            List<MaintenanceRequestOverviewDTO> data = scheduleMaintenanceAndRequestMaterialsService.getAllMaintenanceRequestsOverview();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/details/{equipmentId}")
    public ResponseEntity<?> getMaintenanceRequestDetails(@PathVariable String equipmentId) {
        try {
            MaintenanceRequestDetailDTO dto =
                    scheduleMaintenanceAndRequestMaterialsService.getMaintenanceRequestDetails(equipmentId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to load maintenance request details: " + e.getMessage());
        }
    }

    @GetMapping("/{equipmentId}/check-inventory")
    public ResponseEntity<?> checkInventoryAvailability(@PathVariable String equipmentId) {
        try {
            Map<String, Object> result =
                    scheduleMaintenanceAndRequestMaterialsService.checkInventoryAvailability(equipmentId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Approve maintenance request
    @PostMapping("/{equipmentId}/approve")
    public ResponseEntity<MaintenanceRequestActionResponseDTO> approveMaintenanceRequest(
            @PathVariable String equipmentId,
            @RequestBody(required = false) MaintenanceRequestStatusUpdateDTO statusUpdateDTO) {

        try {
            if (statusUpdateDTO == null) {
                statusUpdateDTO = new MaintenanceRequestStatusUpdateDTO();
            }
            statusUpdateDTO.setEquipmentId(equipmentId);
            statusUpdateDTO.setStatus("APPROVED");

            MaintenanceRequestActionResponseDTO response =
                    scheduleMaintenanceAndRequestMaterialsService.updateMaintenanceRequestStatus(statusUpdateDTO);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            MaintenanceRequestActionResponseDTO errorResponse =
                    MaintenanceRequestActionResponseDTO.builder()
                            .success(false)
                            .message("Failed to approve request: " + e.getMessage())
                            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Reject maintenance request
    @PostMapping("/{equipmentId}/reject")
    public ResponseEntity<MaintenanceRequestActionResponseDTO> rejectMaintenanceRequest(
            @PathVariable String equipmentId,
            @RequestBody(required = false) MaintenanceRequestStatusUpdateDTO statusUpdateDTO) {

        try {
            if (statusUpdateDTO == null) {
                statusUpdateDTO = new MaintenanceRequestStatusUpdateDTO();
            }
            statusUpdateDTO.setEquipmentId(equipmentId);
            statusUpdateDTO.setStatus("REJECTED");

            MaintenanceRequestActionResponseDTO response =
                    scheduleMaintenanceAndRequestMaterialsService.updateMaintenanceRequestStatus(statusUpdateDTO);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            MaintenanceRequestActionResponseDTO errorResponse =
                    MaintenanceRequestActionResponseDTO.builder()
                            .success(false)
                            .message("Failed to reject request: " + e.getMessage())
                            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Update inventory for approved request
    // Update inventory for approved request - FIXED VERSION
    @PostMapping("/{equipmentId}/update-inventory")
    public ResponseEntity<MaintenanceRequestActionResponseDTO> updateInventoryForRequest(
            @PathVariable String equipmentId) {

        try {
            // First check if request is approved - FIXED: use the autowired repository instance
            Equipment_scheduling equipment = equipmentSchedulingRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("Equipment not found"));

            if (!"APPROVED".equalsIgnoreCase(equipment.getStatus())) {
                MaintenanceRequestActionResponseDTO errorResponse =
                        MaintenanceRequestActionResponseDTO.builder()
                                .success(false)
                                .message("Cannot update inventory for non-approved request")
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            MaintenanceRequestStatusUpdateDTO statusUpdateDTO = new MaintenanceRequestStatusUpdateDTO();
            statusUpdateDTO.setEquipmentId(equipmentId);
            statusUpdateDTO.setStatus("APPROVED");

            MaintenanceRequestActionResponseDTO response =
                    scheduleMaintenanceAndRequestMaterialsService.updateMaintenanceRequestStatus(statusUpdateDTO);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            MaintenanceRequestActionResponseDTO errorResponse =
                    MaintenanceRequestActionResponseDTO.builder()
                            .success(false)
                            .message("Failed to update inventory: " + e.getMessage())
                            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}