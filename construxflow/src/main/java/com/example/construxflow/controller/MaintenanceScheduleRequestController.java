package com.example.construxflow.controller;

import com.example.construxflow.dto.MaintenanceScheduleRequestDTO;
import com.example.construxflow.entity.MaintenanceScheduleRequest;
import com.example.construxflow.entity.MaintenanceRequestStatus;
import com.example.construxflow.service.MaintenanceScheduleRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/maintenance-schedule-requests")
@RequiredArgsConstructor
public class MaintenanceScheduleRequestController {

    private final MaintenanceScheduleRequestService maintenanceRequestService;

    @PostMapping("/create")
    public ResponseEntity<MaintenanceScheduleRequest> createMaintenanceRequest(@RequestBody MaintenanceScheduleRequestDTO requestDTO) {
        try {
            MaintenanceScheduleRequest request = maintenanceRequestService.createMaintenanceRequest(requestDTO);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-conflict")
    public ResponseEntity<Map<String, Object>> checkMaintenanceConflict(
            @RequestParam Long equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            boolean hasConflict = maintenanceRequestService.hasMaintenanceConflict(equipmentId, startDate, endDate);
            return ResponseEntity.ok(Map.of("hasConflict", hasConflict));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("hasConflict", false, "error", e.getMessage()));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<MaintenanceScheduleRequest>> getPendingRequests() {
        List<MaintenanceScheduleRequest> requests = maintenanceRequestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<MaintenanceScheduleRequest>> getRequestsByEquipmentId(@PathVariable Long equipmentId) {
        List<MaintenanceScheduleRequest> requests = maintenanceRequestService.getRequestsByEquipmentId(equipmentId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaintenanceScheduleRequest>> getAllMaintenanceRequests() {
        try {
            List<MaintenanceScheduleRequest> requests = maintenanceRequestService.getAllMaintenanceRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{requestId}/status")
    public ResponseEntity<MaintenanceScheduleRequest> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestParam MaintenanceRequestStatus status) {
        try {
            MaintenanceScheduleRequest request = maintenanceRequestService.updateRequestStatus(requestId, status);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}