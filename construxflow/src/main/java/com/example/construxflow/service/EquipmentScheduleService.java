package com.example.construxflow.service;

import com.example.construxflow.dto.EquipmentScheduleDTO;
import com.example.construxflow.dto.ScheduleFormDataDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.EquipmentSchedule;
import com.example.construxflow.entity.EquipmentStatus;
import com.example.construxflow.entity.ScheduleStatus;
import com.example.construxflow.repository.EquipmentRepository;
import com.example.construxflow.repository.EquipmentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentScheduleService {

    private final EquipmentScheduleRepository scheduleRepository;
    private final EquipmentRepository equipmentRepository;

    public ScheduleFormDataDTO getScheduleFormData(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found"));

        return ScheduleFormDataDTO.builder()
                .equipmentId(equipment.getId())
                .equipmentName(equipment.getName())
                .equipmentType(equipment.getType())
                .nextMaintenance(equipment.getNextMaintenance())
                .currentStatus(equipment.getStatus().name())
                .build();
    }

    public EquipmentSchedule createSchedule(EquipmentScheduleDTO scheduleDTO) {
        // Validate equipment exists
        Equipment equipment = equipmentRepository.findById(scheduleDTO.getEquipmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found"));

        // Check if equipment is available
        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipment is not available for scheduling");
        }

        // Check for scheduling conflicts
        if (scheduleRepository.hasSchedulingConflict(scheduleDTO.getEquipmentId(), scheduleDTO.getStartDate(), scheduleDTO.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipment already scheduled for the selected dates");
        }

        // Validate dates
        if (scheduleDTO.getStartDate().isAfter(scheduleDTO.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }

        if (scheduleDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be in the past");
        }

        // Create schedule
        EquipmentSchedule schedule = EquipmentSchedule.builder()
                .equipment(equipment)
                .siteName(scheduleDTO.getSiteName())
                .startDate(scheduleDTO.getStartDate())
                .endDate(scheduleDTO.getEndDate())
                .status(ScheduleStatus.SCHEDULED)
                .notes(scheduleDTO.getNotes())
                .build();

        // Update equipment status
        equipment.setStatus(EquipmentStatus.ON_A_SITE);
        equipmentRepository.save(equipment);

        return scheduleRepository.save(schedule);
    }

    public List<EquipmentSchedule> getSchedulesByEquipmentId(Long equipmentId) {
        return scheduleRepository.findByEquipmentId(equipmentId);
    }

    private boolean hasMaintenanceConflict(Long equipmentId, LocalDate startDate, LocalDate endDate) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found"));

        // Check if equipment has next maintenance date
        if (equipment.getNextMaintenance() == null || equipment.getNextMaintenance().isBlank()) {
            return false;
        }

        try {
            // Parse next maintenance date (assuming format yyyy-MM-dd)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate nextMaintenanceDate = LocalDate.parse(equipment.getNextMaintenance(), formatter);

            // Check if next maintenance falls within the scheduled period
            return !nextMaintenanceDate.isBefore(startDate) && !nextMaintenanceDate.isAfter(endDate);
        } catch (DateTimeParseException e) {
            // If date parsing fails, return false
            return false;
        }
    }

    public Map<String, Object> checkSchedulingConstraints(Long equipmentId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> constraints = new HashMap<>();

        // Check for scheduling conflicts
        boolean hasSchedulingConflict = scheduleRepository.hasSchedulingConflict(equipmentId, startDate, endDate);
        constraints.put("hasSchedulingConflict", hasSchedulingConflict);

        // Check for maintenance conflicts using the utility method
        boolean hasMaintenanceConflict = hasMaintenanceConflict(equipmentId, startDate, endDate);
        constraints.put("hasMaintenanceConflict", hasMaintenanceConflict);

        // Get equipment for additional info
        Equipment equipment = equipmentRepository.findById(equipmentId).orElse(null);
        if (equipment != null) {
            constraints.put("nextMaintenance", equipment.getNextMaintenance());
            constraints.put("equipmentName", equipment.getName());
            constraints.put("currentStatus", equipment.getStatus().name());
        }

        return constraints;
    }
}
