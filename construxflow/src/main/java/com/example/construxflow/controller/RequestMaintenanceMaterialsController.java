package com.example.construxflow.controller;

import com.example.construxflow.dto.RequestMaintenanceMaterialsRequestDTO;
import com.example.construxflow.dto.RequestMaintenanceMaterialsResponseDTO;
import com.example.construxflow.service.RequestMaintenanceMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maintenance-requests")
@CrossOrigin(origins = "http://localhost:")
public class RequestMaintenanceMaterialsController {

    @Autowired
    private RequestMaintenanceMaterialsService requestMaintenanceMaterialsService;

    // Create new maintenance material request
    @PostMapping
    public ResponseEntity<RequestMaintenanceMaterialsResponseDTO> createMaintenanceRequest(
            @RequestBody RequestMaintenanceMaterialsRequestDTO requestDTO) {
        try {
            RequestMaintenanceMaterialsResponseDTO response = requestMaintenanceMaterialsService.createMaintenanceRequest(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get maintenance request by ID
    @GetMapping("/{id}")
    public ResponseEntity<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestById(@PathVariable String id) {
        Optional<RequestMaintenanceMaterialsResponseDTO> request = requestMaintenanceMaterialsService.getMaintenanceRequestById(id);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all maintenance requests
    @GetMapping
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getAllMaintenanceRequests() {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getAllMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }

    // Update maintenance request
    @PutMapping("/{id}")
    public ResponseEntity<RequestMaintenanceMaterialsResponseDTO> updateMaintenanceRequest(
            @PathVariable String id,
            @RequestBody RequestMaintenanceMaterialsRequestDTO requestDTO) {
        try {
            RequestMaintenanceMaterialsResponseDTO response = requestMaintenanceMaterialsService.updateMaintenanceRequest(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete maintenance request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRequest(@PathVariable String id) {
        try {
            requestMaintenanceMaterialsService.deleteMaintenanceRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get maintenance requests by equipment ID
    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMaintenanceRequestsByEquipmentId(
            @PathVariable String equipmentId) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentId(equipmentId);
        return ResponseEntity.ok(requests);
    }

    // Get maintenance requests by item ID
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMaintenanceRequestsByItemId(
            @PathVariable String itemId) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMaintenanceRequestsByItemId(itemId);
        return ResponseEntity.ok(requests);
    }

    // Get maintenance requests by urgency level
    @GetMapping("/urgency/{urgency}")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMaintenanceRequestsByUrgency(
            @PathVariable String urgency) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMaintenanceRequestsByUrgency(urgency);
        return ResponseEntity.ok(requests);
    }

    // Search maintenance requests by item name
    @GetMapping("/search/item-name")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> searchMaintenanceRequestsByItemName(
            @RequestParam String itemName) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.searchMaintenanceRequestsByItemName(itemName);
        return ResponseEntity.ok(requests);
    }

    // Get maintenance requests by equipment ID and urgency
    @GetMapping("/equipment/{equipmentId}/urgency/{urgency}")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMaintenanceRequestsByEquipmentAndUrgency(
            @PathVariable String equipmentId,
            @PathVariable String urgency) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentAndUrgency(equipmentId, urgency);
        return ResponseEntity.ok(requests);
    }

    // Get maintenance requests with quantity greater than specified value
    @GetMapping("/quantity-greater-than")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMaintenanceRequestsByQuantityGreaterThan(
            @RequestParam Number quantity) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMaintenanceRequestsByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(requests);
    }

    // Get maintenance requests by equipment type
    @GetMapping("/equipment-type/{equipmentType}")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMaintenanceRequestsByEquipmentType(
            @PathVariable String equipmentType) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentType(equipmentType);
        return ResponseEntity.ok(requests);
    }

    // Get maintenance requests by equipment name
    @GetMapping("/equipment-name/{equipmentName}")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMaintenanceRequestsByEquipmentName(
            @PathVariable String equipmentName) {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentName(equipmentName);
        return ResponseEntity.ok(requests);
    }

    // Count maintenance requests by equipment ID
    @GetMapping("/equipment/{equipmentId}/count")
    public ResponseEntity<Long> countMaintenanceRequestsByEquipmentId(@PathVariable String equipmentId) {
        long count = requestMaintenanceMaterialsService.countMaintenanceRequestsByEquipmentId(equipmentId);
        return ResponseEntity.ok(count);
    }

    // Check if maintenance request exists for specific equipment and item
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsMaintenanceRequestForEquipmentAndItem(
            @RequestParam String equipmentId,
            @RequestParam String itemId) {
        boolean exists = requestMaintenanceMaterialsService.existsMaintenanceRequestForEquipmentAndItem(equipmentId, itemId);
        return ResponseEntity.ok(exists);
    }

    // Get high priority maintenance requests
    @GetMapping("/priority/high")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getHighPriorityMaintenanceRequests() {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getHighPriorityMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }

    // Get medium priority maintenance requests
    @GetMapping("/priority/medium")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getMediumPriorityMaintenanceRequests() {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getMediumPriorityMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }

    // Get low priority maintenance requests
    @GetMapping("/priority/low")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> getLowPriorityMaintenanceRequests() {
        List<RequestMaintenanceMaterialsResponseDTO> requests = requestMaintenanceMaterialsService.getLowPriorityMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }



    // Bulk create maintenance requests
    @PostMapping("/bulk")
    public ResponseEntity<List<RequestMaintenanceMaterialsResponseDTO>> createBulkMaintenanceRequests(
            @RequestBody List<RequestMaintenanceMaterialsRequestDTO> requestDTOs) {
        try {
            List<RequestMaintenanceMaterialsResponseDTO> responses = requestMaintenanceMaterialsService.createBulkMaintenanceRequests(requestDTOs);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Exception handler for general errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }
}
