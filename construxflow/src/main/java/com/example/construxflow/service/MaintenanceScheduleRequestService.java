package com.example.construxflow.service;

import com.example.construxflow.dto.MaintenanceScheduleRequestDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.MaintenanceScheduleRequest;
import com.example.construxflow.entity.MaintenanceRequestStatus;
import com.example.construxflow.repository.EquipmentRepository;
import com.example.construxflow.repository.MaintenanceScheduleRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceScheduleRequestService {

    private final MaintenanceScheduleRequestRepository maintenanceRequestRepository;
    private final EquipmentRepository equipmentRepository;

    public MaintenanceScheduleRequest createMaintenanceRequest(MaintenanceScheduleRequestDTO requestDTO) {
        // Validate equipment exists
        Equipment equipment = equipmentRepository.findById(requestDTO.getEquipmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found"));

        // Create maintenance request
        MaintenanceScheduleRequest maintenanceRequest = MaintenanceScheduleRequest.builder()
                .equipment(equipment)
                .equipmentName(equipment.getName())
                .equipmentType(equipment.getType())
                .reason(requestDTO.getReason())
                .notes(requestDTO.getNotes())
                .status(MaintenanceRequestStatus.PENDING)
                .build();

        return maintenanceRequestRepository.save(maintenanceRequest);
    }

    public boolean hasMaintenanceConflict(Long equipmentId, LocalDate startDate, LocalDate endDate) {
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

    public List<MaintenanceScheduleRequest> getPendingRequests() {
        return maintenanceRequestRepository.findByStatus(MaintenanceRequestStatus.PENDING);
    }

    public List<MaintenanceScheduleRequest> getRequestsByEquipmentId(Long equipmentId) {
        return maintenanceRequestRepository.findByEquipmentId(equipmentId);
    }

    public List<MaintenanceScheduleRequest> getAllMaintenanceRequests() {
        return maintenanceRequestRepository.findAll();
    }

    public MaintenanceScheduleRequest updateRequestStatus(Long requestId, MaintenanceRequestStatus status) {
        MaintenanceScheduleRequest request = maintenanceRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance request not found"));

        request.setStatus(status);
        return maintenanceRequestRepository.save(request);
    }
}