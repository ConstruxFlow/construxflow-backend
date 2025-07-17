package com.example.construxflow.service;

import com.example.construxflow.dto.EquipmentSchedulingRequestDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.dto.RequestMaintenanceMaterialsResponseDTO;
import com.example.construxflow.entity.Equipment_scheduling;
import com.example.construxflow.entity.Request_manintenance_materials;
import com.example.construxflow.repository.EquipmentSchedulingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EquipmentSchedulingService {

    @Autowired
    private EquipmentSchedulingRepository equipmentSchedulingRepository;

    // Create new equipment scheduling
    public EquipmentSchedulingResponseDTO createEquipmentScheduling(EquipmentSchedulingRequestDTO requestDTO) {
        Equipment_scheduling equipment = new Equipment_scheduling();
        equipment.setId(requestDTO.getId());
        equipment.setEquipmentType(requestDTO.getEquipmentType());
        equipment.setEquipmentName(requestDTO.getEquipmentName());
        equipment.setMaintenanceType(requestDTO.getMaintenanceType());
        equipment.setPriority(requestDTO.getPriority());
        equipment.setDate(requestDTO.getDate());
        equipment.setTime(requestDTO.getTime());
        equipment.setDescription(requestDTO.getDescription());
        equipment.setStatus(requestDTO.getStatus());

        Equipment_scheduling savedEquipment = equipmentSchedulingRepository.save(equipment);
        return mapToResponseDTO(savedEquipment);
    }

    // Get equipment scheduling by ID
    public Optional<EquipmentSchedulingResponseDTO> getEquipmentSchedulingById(String id) {
        return equipmentSchedulingRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    // Get all equipment scheduling
    public List<EquipmentSchedulingResponseDTO> getAllEquipmentScheduling() {
        return equipmentSchedulingRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Update equipment scheduling
    public EquipmentSchedulingResponseDTO updateEquipmentScheduling(String id, EquipmentSchedulingRequestDTO requestDTO) {
        Optional<Equipment_scheduling> existingEquipment = equipmentSchedulingRepository.findById(id);

        if (existingEquipment.isPresent()) {
            Equipment_scheduling equipment = existingEquipment.get();
            equipment.setEquipmentType(requestDTO.getEquipmentType());
            equipment.setEquipmentName(requestDTO.getEquipmentName());
            equipment.setMaintenanceType(requestDTO.getMaintenanceType());
            equipment.setPriority(requestDTO.getPriority());
            equipment.setDate(requestDTO.getDate());
            equipment.setTime(requestDTO.getTime());
            equipment.setDescription(requestDTO.getDescription());

            Equipment_scheduling updatedEquipment = equipmentSchedulingRepository.save(equipment);
            return mapToResponseDTO(updatedEquipment);
        } else {
            throw new RuntimeException("Equipment scheduling not found with id: " + id);
        }
    }

    // Delete equipment scheduling
    public void deleteEquipmentScheduling(String id) {
        if (equipmentSchedulingRepository.existsById(id)) {
            equipmentSchedulingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Equipment scheduling not found with id: " + id);
        }
    }

    // Get equipment by type
    public List<EquipmentSchedulingResponseDTO> getEquipmentByType(String equipmentType) {
        return equipmentSchedulingRepository.findByEquipmentType(equipmentType)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment by name
    public List<EquipmentSchedulingResponseDTO> getEquipmentByName(String equipmentName) {
        return equipmentSchedulingRepository.findByEquipmentName(equipmentName)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment scheduled for a specific date
    public List<EquipmentSchedulingResponseDTO> getEquipmentByDate(Date date) {
        return equipmentSchedulingRepository.findByDate(date)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment scheduled within date range
    public List<EquipmentSchedulingResponseDTO> getEquipmentByDateRange(Date startDate, Date endDate) {
        return equipmentSchedulingRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment scheduled for today
    public List<EquipmentSchedulingResponseDTO> getEquipmentScheduledForToday() {
        return equipmentSchedulingRepository.findScheduledForToday()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment by time range
    public List<EquipmentSchedulingResponseDTO> getEquipmentByTimeRange(Time startTime, Time endTime) {
        return equipmentSchedulingRepository.findByTimeRange(startTime, endTime)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment with maintenance requests
    public List<EquipmentSchedulingResponseDTO> getEquipmentWithMaintenanceRequests() {
        return equipmentSchedulingRepository.findEquipmentWithMaintenanceRequests()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment without maintenance requests
    public List<EquipmentSchedulingResponseDTO> getEquipmentWithoutMaintenanceRequests() {
        return equipmentSchedulingRepository.findEquipmentWithoutMaintenanceRequests()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Search equipment by name (partial match)
    public List<EquipmentSchedulingResponseDTO> searchEquipmentByName(String searchTerm) {
        return equipmentSchedulingRepository.findByEquipmentNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Search equipment by description
    public List<EquipmentSchedulingResponseDTO> searchEquipmentByDescription(String searchTerm) {
        return equipmentSchedulingRepository.findByDescriptionContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment by type ordered by date
    public List<EquipmentSchedulingResponseDTO> getEquipmentByTypeOrderedByDate(String equipmentType) {
        return equipmentSchedulingRepository.findByEquipmentTypeOrderByDateAsc(equipmentType)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Check if equipment is scheduled for specific date and time
    public boolean isEquipmentScheduled(Date date, Time time) {
        return equipmentSchedulingRepository.existsByDateAndTime(date, time);
    }

    // Get equipment scheduled after a specific date
    public List<EquipmentSchedulingResponseDTO> getEquipmentScheduledAfter(Date date) {
        return equipmentSchedulingRepository.findByDateAfter(date)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment scheduled before a specific date
    public List<EquipmentSchedulingResponseDTO> getEquipmentScheduledBefore(Date date) {
        return equipmentSchedulingRepository.findByDateBefore(date)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get equipment with maintenance request count
    public List<Object[]> getEquipmentWithMaintenanceRequestCount() {
        return equipmentSchedulingRepository.findEquipmentWithMaintenanceRequestCount();
    }

    @Transactional
    public EquipmentSchedulingResponseDTO updateStatus(String id, String newStatus) {
        Equipment_scheduling equipmentScheduleEntity = equipmentSchedulingRepository.findById(id).
                orElseThrow(()->new RuntimeException("Equipment scheduling not found with id: " + id));

        equipmentScheduleEntity.setStatus(newStatus);
        Equipment_scheduling updated = equipmentSchedulingRepository.save(equipmentScheduleEntity);

        return mapToResponseDTO(equipmentScheduleEntity);
    }

    // Helper method to map entity to response DTO
    private EquipmentSchedulingResponseDTO mapToResponseDTO(Equipment_scheduling equipment) {
        List<RequestMaintenanceMaterialsResponseDTO> maintenanceRequests = null;

        if (equipment.getMaintenanceRequests() != null) {
            maintenanceRequests = equipment.getMaintenanceRequests()
                    .stream()
                    .map(this::mapMaintenanceRequestToDTO)
                    .collect(Collectors.toList());
        }

        return EquipmentSchedulingResponseDTO.builder()
                .id(equipment.getId())
                .equipmentType(equipment.getEquipmentType())
                .equipmentName(equipment.getEquipmentName())
                .maintenanceType(equipment.getMaintenanceType())
                .priority(equipment.getPriority())
                .date(equipment.getDate())
                .time(equipment.getTime())
                .description(equipment.getDescription())
                .status(equipment.getStatus())
                .maintenanceRequests(maintenanceRequests)
                .build();
    }

    // Helper method to map maintenance request to DTO
    private RequestMaintenanceMaterialsResponseDTO mapMaintenanceRequestToDTO(Request_manintenance_materials request) {
        return RequestMaintenanceMaterialsResponseDTO.builder()
                .id(request.getId())
                .itemId(request.getItemId())
                .itemName(request.getItemName())
                .quantity(request.getQuantity())
                .measurement(request.getMeasurement())
                .justification(request.getJustification())
                .urgency(request.getUrgency())
                .build();
    }
}
