package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentDTO;
import com.example.construxflow.dto.EquipmentListItemDTO;
import com.example.construxflow.dto.EquipmentStatsDTO;
import com.example.construxflow.dto.PagedResponse;
import com.example.construxflow.dto.NextEquipmentScheduleResponseDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.EquipmentSchedule;
import com.example.construxflow.entity.EquipmentStatus;
import com.example.construxflow.entity.ScheduleStatus;
import com.example.construxflow.service.EquipmentScheduleService;
import com.example.construxflow.service.EquipmentService;
import com.example.construxflow.service.NextEquipmentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final NextEquipmentScheduleService nextEquipmentScheduleService;
    private final EquipmentScheduleService equipmentScheduleService;

    // --------------------- EXISTING ENDPOINTS (unchanged) ---------------------

    @PostMapping("/add")
    public Equipment addEquipment(@RequestBody EquipmentDTO dto) {
        return equipmentService.addEquipment(dto);
    }

    @GetMapping("/all")
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return equipment != null ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-id")
    public ResponseEntity<Equipment> getEquipmentByIdParam(@RequestParam Long id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return equipment != null ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }

    // GET /api/equipment/scheduling?search=...&status=AVAILABLE|UNDER_MAINTENANCE|ON_A_SITE
    @GetMapping("/scheduling")
    public List<EquipmentListItemDTO> listForScheduling(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EquipmentStatus status
    ) {
        return equipmentService.listForScheduling(search, status);
    }

    // Optional: your "scheduled-equipment-details" endpoint
    @GetMapping("/scheduled-equipment-details")
    public ResponseEntity<List<Equipment>> getScheduledEquipmentDetails() {
        List<NextEquipmentScheduleResponseDTO> schedules = nextEquipmentScheduleService.getAllNextScheduleDetails();
        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Equipment> equipmentDetails = schedules.stream()
                .map(schedule -> {
                    try {
                        Long equipmentId = Long.parseLong(schedule.getEquipmentId());
                        return equipmentService.getEquipmentById(equipmentId);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(e -> e != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(equipmentDetails);
    }

    // --------------------- NEW ENDPOINTS (added; do not break existing) ---------------------

    // KPI cards for your dashboard
    @GetMapping("/stats")
    public ResponseEntity<EquipmentStatsDTO> stats() {
        return ResponseEntity.ok(equipmentService.stats());
    }

    /**
     * Paginated + searchable + filterable list for your table/cards,
     * but returns DTOs (UI-friendly) instead of entities.
     * <p>
     * Example:
     * GET /api/equipment/search?search=excavator&status=In%20Use&page=0&size=10&sortBy=name&sortDir=asc
     * status can be: "All" | "Available" | "Under Maintenance" | "In Use"
     */
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<EquipmentListItemDTO>> searchListItems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "All") String status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(equipmentService.searchListItems(search, status, page, size, sortBy, sortDir));
    }

    // Add these endpoints to your existing EquipmentController

    // Update equipment stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Equipment> updateEquipmentStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        try {
            Equipment equipment = equipmentService.updateEquipmentStock(id, quantity);
            return ResponseEntity.ok(equipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Search equipment by name
    @GetMapping("/search-by-name")
    public ResponseEntity<List<Equipment>> searchEquipmentByName(@RequestParam String name) {
        List<Equipment> equipment = equipmentService.searchEquipmentByName(name);
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEquipment(@PathVariable Long id) {
        try {
            boolean isDeleted = equipmentService.deleteEquipment(id);
            if (isDeleted) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Equipment deleted successfully");
                response.put("deletedId", id.toString());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to delete equipment"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/{id}/schedule-status")
    public ResponseEntity<Map<String, Object>> getEquipmentScheduleStatus(@PathVariable Long id) {
        try {
            Equipment equipment = equipmentService.getEquipmentById(id);
            if (equipment == null) {
                return ResponseEntity.notFound().build();
            }

            List<EquipmentSchedule> schedules = equipmentScheduleService.getSchedulesByEquipmentId(id);
            boolean hasActiveSchedule = schedules.stream()
                    .anyMatch(schedule -> schedule.getStatus() == ScheduleStatus.SCHEDULED &&
                            schedule.getEndDate().isAfter(LocalDate.now()));

            Map<String, Object> response = new HashMap<>();
            response.put("equipment", equipment);
            response.put("hasActiveSchedule", hasActiveSchedule);
            response.put("schedules", schedules);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }}
