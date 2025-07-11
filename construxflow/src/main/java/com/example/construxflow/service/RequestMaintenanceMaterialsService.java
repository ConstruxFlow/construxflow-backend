package com.example.construxflow.service;

import com.example.construxflow.dto.RequestMaintenanceMaterialsRequestDTO;
import com.example.construxflow.dto.RequestMaintenanceMaterialsResponseDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.entity.Request_manintenance_materials;
import com.example.construxflow.entity.Equipment_scheduling;
import com.example.construxflow.repository.RequestMaintenanceMaterialsRepository;
import com.example.construxflow.repository.EquipmentSchedulingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RequestMaintenanceMaterialsService {

    @Autowired
    private RequestMaintenanceMaterialsRepository requestMaintenanceMaterialsRepository;

    @Autowired
    private EquipmentSchedulingRepository equipmentSchedulingRepository;

    // Create new maintenance material request
    public RequestMaintenanceMaterialsResponseDTO createMaintenanceRequest(RequestMaintenanceMaterialsRequestDTO requestDTO) {
        // Validate equipment exists
        Optional<Equipment_scheduling> equipment = equipmentSchedulingRepository.findById(requestDTO.getEquipmentId());
        if (equipment.isEmpty()) {
            throw new RuntimeException("Equipment not found with id: " + requestDTO.getEquipmentId());
        }

        Request_manintenance_materials request = new Request_manintenance_materials();
        request.setId(requestDTO.getId());
        request.setEquipment(equipment.get());
        request.setItemId(requestDTO.getItemId());
        request.setItemName(requestDTO.getItemName());
        request.setQuantity(requestDTO.getQuantity());
        request.setMeasurement(requestDTO.getMeasurement());
        request.setJustification(requestDTO.getJustification());
        request.setUrgency(requestDTO.getUrgency());

        Request_manintenance_materials savedRequest = requestMaintenanceMaterialsRepository.save(request);
        return mapToResponseDTO(savedRequest);
    }

    // Get maintenance request by ID
    public Optional<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestById(String id) {
        return requestMaintenanceMaterialsRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    // Get all maintenance requests
    public List<RequestMaintenanceMaterialsResponseDTO> getAllMaintenanceRequests() {
        return requestMaintenanceMaterialsRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Update maintenance request
    public RequestMaintenanceMaterialsResponseDTO updateMaintenanceRequest(String id, RequestMaintenanceMaterialsRequestDTO requestDTO) {
        Optional<Request_manintenance_materials> existingRequest = requestMaintenanceMaterialsRepository.findById(id);

        if (existingRequest.isPresent()) {
            Request_manintenance_materials request = existingRequest.get();

            // Update equipment if changed
            if (!request.getEquipment().getId().equals(requestDTO.getEquipmentId())) {
                Optional<Equipment_scheduling> equipment = equipmentSchedulingRepository.findById(requestDTO.getEquipmentId());
                if (equipment.isEmpty()) {
                    throw new RuntimeException("Equipment not found with id: " + requestDTO.getEquipmentId());
                }
                request.setEquipment(equipment.get());
            }

            request.setItemId(requestDTO.getItemId());
            request.setItemName(requestDTO.getItemName());
            request.setQuantity(requestDTO.getQuantity());
            request.setJustification(requestDTO.getJustification());
            request.setUrgency(requestDTO.getUrgency());

            Request_manintenance_materials updatedRequest = requestMaintenanceMaterialsRepository.save(request);
            return mapToResponseDTO(updatedRequest);
        } else {
            throw new RuntimeException("Maintenance request not found with id: " + id);
        }
    }

    // Delete maintenance request
    public void deleteMaintenanceRequest(String id) {
        if (requestMaintenanceMaterialsRepository.existsById(id)) {
            requestMaintenanceMaterialsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Maintenance request not found with id: " + id);
        }
    }

    // Get maintenance requests by equipment ID
    public List<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestsByEquipmentId(String equipmentId) {
        return requestMaintenanceMaterialsRepository.findByEquipmentId(equipmentId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get maintenance requests by item ID
    public List<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestsByItemId(String itemId) {
        return requestMaintenanceMaterialsRepository.findByItemId(itemId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get maintenance requests by urgency level
    public List<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestsByUrgency(String urgency) {
        return requestMaintenanceMaterialsRepository.findByUrgency(urgency)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Search maintenance requests by item name
    public List<RequestMaintenanceMaterialsResponseDTO> searchMaintenanceRequestsByItemName(String itemName) {
        return requestMaintenanceMaterialsRepository.findByItemNameContainingIgnoreCase(itemName)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get maintenance requests by equipment ID and urgency
    public List<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestsByEquipmentAndUrgency(String equipmentId, String urgency) {
        return requestMaintenanceMaterialsRepository.findByEquipmentIdAndUrgency(equipmentId, urgency)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get maintenance requests with quantity greater than specified value
    public List<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestsByQuantityGreaterThan(Number quantity) {
        return requestMaintenanceMaterialsRepository.findByQuantityGreaterThan(quantity)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get maintenance requests by equipment type
    public List<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestsByEquipmentType(String equipmentType) {
        return requestMaintenanceMaterialsRepository.findByEquipmentType(equipmentType)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get maintenance requests by equipment name
    public List<RequestMaintenanceMaterialsResponseDTO> getMaintenanceRequestsByEquipmentName(String equipmentName) {
        return requestMaintenanceMaterialsRepository.findByEquipmentName(equipmentName)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Count maintenance requests by equipment ID
    public long countMaintenanceRequestsByEquipmentId(String equipmentId) {
        return requestMaintenanceMaterialsRepository.countByEquipmentId(equipmentId);
    }

    // Check if maintenance request exists for specific equipment and item
    public boolean existsMaintenanceRequestForEquipmentAndItem(String equipmentId, String itemId) {
        return requestMaintenanceMaterialsRepository.existsByEquipmentIdAndItemId(equipmentId, itemId);
    }

    // Get high priority maintenance requests (assuming "HIGH" urgency)
    public List<RequestMaintenanceMaterialsResponseDTO> getHighPriorityMaintenanceRequests() {
        return getMaintenanceRequestsByUrgency("HIGH");
    }

    // Get medium priority maintenance requests
    public List<RequestMaintenanceMaterialsResponseDTO> getMediumPriorityMaintenanceRequests() {
        return getMaintenanceRequestsByUrgency("MEDIUM");
    }

    // Get low priority maintenance requests
    public List<RequestMaintenanceMaterialsResponseDTO> getLowPriorityMaintenanceRequests() {
        return getMaintenanceRequestsByUrgency("LOW");
    }



    // Helper method to get urgency priority for sorting
    private String getUrgencyPriority(String urgency) {
        switch (urgency.toUpperCase()) {
            case "HIGH": return "1";
            case "MEDIUM": return "2";
            case "LOW": return "3";
            default: return "4";
        }
    }

    // Bulk create maintenance requests
    public List<RequestMaintenanceMaterialsResponseDTO> createBulkMaintenanceRequests(List<RequestMaintenanceMaterialsRequestDTO> requestDTOs) {
        return requestDTOs.stream()
                .map(this::createMaintenanceRequest)
                .collect(Collectors.toList());
    }

    // Helper method to map entity to response DTO
    private RequestMaintenanceMaterialsResponseDTO mapToResponseDTO(Request_manintenance_materials request) {
        EquipmentSchedulingResponseDTO equipmentDTO = null;

        if (request.getEquipment() != null) {
            equipmentDTO = EquipmentSchedulingResponseDTO.builder()
                    .id(request.getEquipment().getId())
                    .equipmentType(request.getEquipment().getEquipmentType())
                    .equipmentName(request.getEquipment().getEquipmentName())
                    .date(request.getEquipment().getDate())
                    .time(request.getEquipment().getTime())
                    .description(request.getEquipment().getDescription())
                    .build();
        }

        return RequestMaintenanceMaterialsResponseDTO.builder()
                .id(request.getId())
                .itemId(request.getItemId())
                .itemName(request.getItemName())
                .quantity(request.getQuantity())
                .justification(request.getJustification())
                .urgency(request.getUrgency())
                .build();
    }
}
