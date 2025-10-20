package com.example.construxflow.controller;


import com.example.construxflow.dto.*;
import com.example.construxflow.entity.*;
import com.example.construxflow.repository.EquipmentSchedulingRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final NextEquipmentScheduleService nextEquipmentScheduleService;
    private final EquipmentScheduleService equipmentScheduleService;


    @Autowired
    private EquipmentSchedulingRepository equipmentSchedulingRepository;
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

    }
    @GetMapping("/details/{equipmentId}")
    public ResponseEntity<?> getEquipmentDetails(@PathVariable String equipmentId) {
        try {
            // In a real application, you would have an Equipment entity
            // For now, we'll use Equipment_scheduling to simulate equipment details
            Optional<Equipment_scheduling> equipmentOpt = equipmentSchedulingRepository.findById(equipmentId);

            if (equipmentOpt.isPresent()) {
                Equipment_scheduling equipment = equipmentOpt.get();

                EquipmentDetailsDTO details = EquipmentDetailsDTO.builder()
                        .id(Long.parseLong(equipmentId.replaceAll("\\D+", ""))) // Extract numbers from ID
                        .name(equipment.getEquipmentName())
                        .type(equipment.getEquipmentType())
                        .brand("Caterpillar") // Mock data
                        .model("CAT-320") // Mock data
                        .status(equipment.getStatus())
                        .location("Site A - Construction Zone")
                        .lastMaintenance("2024-01-15")
                        .nextMaintenance("2024-04-15")
                        .utilization("75%")
                        .specifications("200 HP, 25 Ton Capacity")
                        .notes("Regular maintenance required every 3 months")
                        .build();

                return ResponseEntity.ok(details);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching equipment details: " + e.getMessage());
        }
    }
    @GetMapping("/schedule/{equipmentId}")
    public ResponseEntity<?> getEquipmentSchedule(@PathVariable String equipmentId) {
        try {
            // Find all schedules for this equipment
            List<Equipment_scheduling> schedules = equipmentSchedulingRepository.findByEquipmentId(Integer.parseInt(equipmentId.replaceAll("\\D+", "")));

            EquipmentScheduleDTO scheduleDTO = new EquipmentScheduleDTO();
            scheduleDTO.setEquipmentId(Long.parseLong(equipmentId.replaceAll("\\D+", "")));
            scheduleDTO.setEquipmentName(schedules.isEmpty() ? "Unknown Equipment" : schedules.get(0).getEquipmentName());

            // Convert schedules to ScheduleItemDTO
            List<ScheduleItemDTO> scheduleItems = schedules.stream().map(schedule ->
                    ScheduleItemDTO.builder()
                            .scheduleId(schedule.getId())
                            .date(schedule.getDate())
                            .time(schedule.getTime() != null ? schedule.getTime().toString() : "09:00")
                            .maintenanceType(schedule.getMaintenanceType())
                            .priority(schedule.getPriority())
                            .status(schedule.getStatus())
                            .assignedTo("Maintenance Team A")
                            .description(schedule.getDescription())
                            .build()
            ).toList();

            scheduleDTO.setScheduleItems(scheduleItems);

            // Mock maintenance history
            List<MaintenanceHistoryDTO> maintenanceHistory = List.of(
                    MaintenanceHistoryDTO.builder()
                            .maintenanceId("MNT-001")
                            .date(new Date(System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000)) // 90 days ago
                            .type("Routine Maintenance")
                            .performedBy("John Smith")
                            .description("Oil change, filter replacement, and general inspection")
                            .status("Completed")
                            .cost(1250.50)
                            .build(),
                    MaintenanceHistoryDTO.builder()
                            .maintenanceId("MNT-002")
                            .date(new Date(System.currentTimeMillis() - 180L * 24 * 60 * 60 * 1000)) // 180 days ago
                            .type("Major Overhaul")
                            .performedBy("Mike Johnson")
                            .description("Engine rebuild and hydraulic system repair")
                            .status("Completed")
                            .cost(8500.75)
                            .build()
            );

            scheduleDTO.setMaintenanceHistory(maintenanceHistory);

            return ResponseEntity.ok(scheduleDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching equipment schedule: " + e.getMessage());
        }
    }
}

