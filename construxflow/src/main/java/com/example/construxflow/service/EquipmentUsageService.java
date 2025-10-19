package com.example.construxflow.service;

import com.example.construxflow.dto.EquipmentLastUsageDTO;
import com.example.construxflow.dto.EquipmentUsageResponseDTO;
import com.example.construxflow.dto.EquipmentUsageSummaryDTO;
import com.example.construxflow.dto.EquipmentUsageUpdateDTO;
import com.example.construxflow.entity.EquipmentUsageLog;
import com.example.construxflow.repository.EquipmentUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipmentUsageService {

    @Autowired
    private EquipmentUsageRepository equipmentUsageRepository;

    public EquipmentUsageResponseDTO updateEquipmentUsage(EquipmentUsageUpdateDTO dto) {
        System.out.println("Updating Equipment Usage Log: " + dto);

        // Add detailed logging to debug the issue
        System.out.println("DTO Details:");
        System.out.println("Equipment ID: " + dto.getEquipmentId());
        System.out.println("Project ID: " + dto.getProjectId());
        System.out.println("Operator: " + dto.getOperator());
        System.out.println("Usage Date: " + dto.getUsageDate());
        System.out.println("Hours Used: " + dto.getHoursUsed());

        // Check if DTO is completely null/empty
        if (dto.getEquipmentId() == null && dto.getProjectId() == null && dto.getOperator() == null) {
            System.err.println("WARNING: Received DTO with all null values!");
            throw new IllegalArgumentException("Equipment usage data cannot be null or empty");
        }

        EquipmentUsageLog equipmentUsageLog = new EquipmentUsageLog();

        // Map DTO fields to entity with null safety
        equipmentUsageLog.setEquipmentId(dto.getEquipmentId());
        equipmentUsageLog.setProjectId(dto.getProjectId());
        equipmentUsageLog.setOperator(dto.getOperator());
        equipmentUsageLog.setUsageDate(dto.getUsageDate());
        equipmentUsageLog.setStartTime(dto.getStartTime());
        equipmentUsageLog.setEndTime(dto.getEndTime());
        equipmentUsageLog.setHoursUsed(dto.getHoursUsed());
        equipmentUsageLog.setKilometersTraveled(dto.getKilometersTraveled());
        equipmentUsageLog.setLocation(dto.getLocation());
        equipmentUsageLog.setPurpose(dto.getPurpose());
        equipmentUsageLog.setNotes(dto.getNotes());
        equipmentUsageLog.setFuelConsumption(dto.getFuelConsumption());
        equipmentUsageLog.setMaintenanceNotes(dto.getMaintenanceNotes());
        equipmentUsageLog.setWeatherConditions(dto.getWeatherConditions());
        equipmentUsageLog.setStatus(dto.getStatus() != null ? dto.getStatus() : "Completed");
        equipmentUsageLog.setLoggedAt(dto.getLoggedAt() != null ? dto.getLoggedAt() : LocalDateTime.now());
        equipmentUsageLog.setLoggedBy(dto.getLoggedBy());

        System.out.println("Entity before save:");
        System.out.println("Equipment ID: " + equipmentUsageLog.getEquipmentId());
        System.out.println("Project ID: " + equipmentUsageLog.getProjectId());

        // Save the equipment usage log
        EquipmentUsageLog savedLog = equipmentUsageRepository.save(equipmentUsageLog);

        System.out.println("Entity after save:");
        System.out.println("ID: " + savedLog.getId());
        System.out.println("Equipment ID: " + savedLog.getEquipmentId());
        System.out.println("Project ID: " + savedLog.getProjectId());

        // Map saved entity back to DTO for response
        return EquipmentUsageResponseDTO.builder()
            .id(savedLog.getId())
            .equipmentId(savedLog.getEquipmentId())
            .projectId(savedLog.getProjectId())
            .operator(savedLog.getOperator())
            .usageDate(savedLog.getUsageDate())
            .startTime(savedLog.getStartTime())
            .endTime(savedLog.getEndTime())
            .hoursUsed(savedLog.getHoursUsed())
            .kilometersTraveled(savedLog.getKilometersTraveled())
            .location(savedLog.getLocation())
            .purpose(savedLog.getPurpose())
            .notes(savedLog.getNotes())
            .fuelConsumption(savedLog.getFuelConsumption())
            .maintenanceNotes(savedLog.getMaintenanceNotes())
            .weatherConditions(savedLog.getWeatherConditions())
            .status(savedLog.getStatus())
            .loggedAt(savedLog.getLoggedAt())
            .loggedBy(savedLog.getLoggedBy())
            .build();
    }

    public List<EquipmentUsageResponseDTO> getAllEquipmentUsageDetails() {
        System.out.println("Fetching all equipment usage details");

        List<EquipmentUsageLog> usageLogs = equipmentUsageRepository.findAll();

        return usageLogs.stream()
            .map(this::convertToResponseDTO)
            .collect(java.util.stream.Collectors.toList());
    }

    private EquipmentUsageResponseDTO convertToResponseDTO(EquipmentUsageLog log) {
        return EquipmentUsageResponseDTO.builder()
            .id(log.getId())
            .equipmentId(log.getEquipmentId())
            .projectId(log.getProjectId())
            .operator(log.getOperator())
            .usageDate(log.getUsageDate())
            .startTime(log.getStartTime())
            .endTime(log.getEndTime())
            .hoursUsed(log.getHoursUsed())
            .kilometersTraveled(log.getKilometersTraveled())
            .location(log.getLocation())
            .purpose(log.getPurpose())
            .notes(log.getNotes())
            .fuelConsumption(log.getFuelConsumption())
            .maintenanceNotes(log.getMaintenanceNotes())
            .weatherConditions(log.getWeatherConditions())
            .status(log.getStatus())
            .loggedAt(log.getLoggedAt())
            .loggedBy(log.getLoggedBy())
            .build();
    }

    public EquipmentUsageSummaryDTO getEquipmentUsageSummary(Long equipmentId) {
        System.out.println("Calculating usage summary for equipment ID: " + equipmentId);

        // Fetch all usage logs for the specific equipment
        List<EquipmentUsageLog> equipmentLogs = equipmentUsageRepository.findByEquipmentId(equipmentId);

        if (equipmentLogs.isEmpty()) {
            return EquipmentUsageSummaryDTO.builder()
                .equipmentId(equipmentId)
                .totalHoursUsed(0.0)
                .totalKilometersTraveled(0.0)
                .totalFuelConsumption(0.0)
                .totalUsageRecords(0)
                .build();
        }

        // Calculate totals
        double totalHours = equipmentLogs.stream()
            .mapToDouble(log -> log.getHoursUsed() != null ? log.getHoursUsed() : 0.0)
            .sum();

        double totalKilometers = equipmentLogs.stream()
            .mapToDouble(log -> log.getKilometersTraveled() != null ? log.getKilometersTraveled() : 0.0)
            .sum();

        // For fuel consumption, parse numeric values from strings like "25.5L"
        double totalFuelConsumption = equipmentLogs.stream()
            .mapToDouble(log -> parseFuelConsumption(log.getFuelConsumption()))
            .sum();

        return EquipmentUsageSummaryDTO.builder()
            .equipmentId(equipmentId)
            .totalHoursUsed(totalHours)
            .totalKilometersTraveled(totalKilometers)
            .totalFuelConsumption(totalFuelConsumption)
            .totalUsageRecords(equipmentLogs.size())
            .build();
    }

    private double parseFuelConsumption(String fuelConsumption) {
        if (fuelConsumption == null || fuelConsumption.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // Remove common units like 'L', 'l', 'liters', etc. and parse the number
            String numericPart = fuelConsumption.replaceAll("[^0-9.]", "").trim();
            return numericPart.isEmpty() ? 0.0 : Double.parseDouble(numericPart);
        } catch (NumberFormatException e) {
            System.err.println("Could not parse fuel consumption: " + fuelConsumption);
            return 0.0;
        }
    }

    public EquipmentLastUsageDTO getEquipmentLastUsage(Long equipmentId) {
        System.out.println("Fetching last usage details for equipment ID: " + equipmentId);

        // Fetch the most recent usage log for the specific equipment
        List<EquipmentUsageLog> equipmentLogs = equipmentUsageRepository.findByEquipmentIdOrderByLoggedAtDesc(equipmentId);

        if (equipmentLogs.isEmpty()) {
            return EquipmentLastUsageDTO.builder()
                .equipmentId(equipmentId)
                .lastUsageHours(0.0)
                .lastUsageLocation("No usage history")
                .lastUsageDate(null)
                .lastLoggedAt(null)
                .operator("N/A")
                .purpose("N/A")
                .status("No usage history")
                .hasUsageHistory(false)
                .build();
        }

        // Get the most recent usage log
        EquipmentUsageLog lastUsage = equipmentLogs.get(0);

        return EquipmentLastUsageDTO.builder()
            .equipmentId(equipmentId)
            .lastUsageHours(lastUsage.getHoursUsed() != null ? lastUsage.getHoursUsed() : 0.0)
            .lastUsageLocation(lastUsage.getLocation() != null ? lastUsage.getLocation() : "Unknown location")
            .lastUsageDate(lastUsage.getUsageDate())
            .lastLoggedAt(lastUsage.getLoggedAt())
            .operator(lastUsage.getOperator())
            .purpose(lastUsage.getPurpose())
            .status(lastUsage.getStatus())
            .hasUsageHistory(true)
            .build();
    }
}
