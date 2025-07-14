package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentSchedulingRequestDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.service.EquipmentSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipment-scheduling")
@CrossOrigin(origins = "http://localhost:")
public class EquipmentSchedulingController {

    @Autowired
    private EquipmentSchedulingService equipmentSchedulingService;

    // Create new equipment scheduling
    @PostMapping
    public ResponseEntity<EquipmentSchedulingResponseDTO> createEquipmentScheduling(
            @RequestBody EquipmentSchedulingRequestDTO requestDTO) {
        try {
            EquipmentSchedulingResponseDTO response = equipmentSchedulingService.createEquipmentScheduling(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get equipment scheduling by ID
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentSchedulingResponseDTO> getEquipmentSchedulingById(@PathVariable String id) {
        Optional<EquipmentSchedulingResponseDTO> equipment = equipmentSchedulingService.getEquipmentSchedulingById(id);
        return equipment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all equipment scheduling
    @GetMapping
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getAllEquipmentScheduling() {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getAllEquipmentScheduling();
        return ResponseEntity.ok(equipmentList);
    }

    // Update equipment scheduling
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentSchedulingResponseDTO> updateEquipmentScheduling(
            @PathVariable String id,
            @RequestBody EquipmentSchedulingRequestDTO requestDTO) {
        try {
            EquipmentSchedulingResponseDTO response = equipmentSchedulingService.updateEquipmentScheduling(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete equipment scheduling
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipmentScheduling(@PathVariable String id) {
        try {
            equipmentSchedulingService.deleteEquipmentScheduling(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get equipment by type
    @GetMapping("/type/{equipmentType}")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentByType(@PathVariable String equipmentType) {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentByType(equipmentType);
        return ResponseEntity.ok(equipmentList);
    }

    // Get equipment by name
    @GetMapping("/name/{equipmentName}")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentByName(@PathVariable String equipmentName) {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentByName(equipmentName);
        return ResponseEntity.ok(equipmentList);
    }

    // Search equipment by name (partial match)
    @GetMapping("/search/name")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> searchEquipmentByName(@RequestParam String searchTerm) {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.searchEquipmentByName(searchTerm);
        return ResponseEntity.ok(equipmentList);
    }

    // Search equipment by description
    @GetMapping("/search/description")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> searchEquipmentByDescription(@RequestParam String searchTerm) {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.searchEquipmentByDescription(searchTerm);
        return ResponseEntity.ok(equipmentList);
    }

    // Get equipment scheduled for a specific date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentByDate(@PathVariable String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentByDate(parsedDate);
            return ResponseEntity.ok(equipmentList);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get equipment scheduled within date range
    @GetMapping("/date-range")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentByDateRange(start, end);
            return ResponseEntity.ok(equipmentList);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get equipment scheduled for today
    @GetMapping("/today")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentScheduledForToday() {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentScheduledForToday();
        return ResponseEntity.ok(equipmentList);
    }

    // Get equipment by time range
    @GetMapping("/time-range")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        try {
            Time start = Time.valueOf(startTime);
            Time end = Time.valueOf(endTime);
            List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentByTimeRange(start, end);
            return ResponseEntity.ok(equipmentList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get equipment with maintenance requests
    @GetMapping("/with-maintenance")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentWithMaintenanceRequests() {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentWithMaintenanceRequests();
        return ResponseEntity.ok(equipmentList);
    }

    // Get equipment without maintenance requests
    @GetMapping("/without-maintenance")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentWithoutMaintenanceRequests() {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentWithoutMaintenanceRequests();
        return ResponseEntity.ok(equipmentList);
    }

    // Get equipment by type ordered by date
    @GetMapping("/type/{equipmentType}/ordered")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentByTypeOrderedByDate(@PathVariable String equipmentType) {
        List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentByTypeOrderedByDate(equipmentType);
        return ResponseEntity.ok(equipmentList);
    }

    // Check if equipment is scheduled for specific date and time
    @GetMapping("/check-schedule")
    public ResponseEntity<Boolean> isEquipmentScheduled(
            @RequestParam String date,
            @RequestParam String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            Time parsedTime = Time.valueOf(time);
            boolean isScheduled = equipmentSchedulingService.isEquipmentScheduled(parsedDate, parsedTime);
            return ResponseEntity.ok(isScheduled);
        } catch (ParseException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get equipment scheduled after a specific date
    @GetMapping("/after/{date}")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentScheduledAfter(@PathVariable String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentScheduledAfter(parsedDate);
            return ResponseEntity.ok(equipmentList);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get equipment scheduled before a specific date
    @GetMapping("/before/{date}")
    public ResponseEntity<List<EquipmentSchedulingResponseDTO>> getEquipmentScheduledBefore(@PathVariable String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            List<EquipmentSchedulingResponseDTO> equipmentList = equipmentSchedulingService.getEquipmentScheduledBefore(parsedDate);
            return ResponseEntity.ok(equipmentList);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get equipment with maintenance request count
    @GetMapping("/maintenance-count")
    public ResponseEntity<List<Object[]>> getEquipmentWithMaintenanceRequestCount() {
        List<Object[]> result = equipmentSchedulingService.getEquipmentWithMaintenanceRequestCount();
        return ResponseEntity.ok(result);
    }

    // Exception handler for general errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }
}
