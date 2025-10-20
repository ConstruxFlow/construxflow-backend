package com.example.construxflow.service;

import com.example.construxflow.dto.*;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.EquipmentRequest;
import com.example.construxflow.entity.Project;
import com.example.construxflow.repository.EquipmentRepository;
import com.example.construxflow.repository.EquipmentRequestRepository;
import com.example.construxflow.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentRequestService {

    private final EquipmentRequestRepository requestRepository;
    private final ProjectRepository projectRepository;
    private final EquipmentRepository equipmentRepository;

    // Get all equipment requests with detailed information
    @Transactional(readOnly = true)
    public List<RequestResponseDTO> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Create new equipment request
    @Transactional
    public EquipmentRequest createRequest(CreateRequestDTO requestDTO) {
        // Validate project exists
        Project project = projectRepository.findByProjectId(requestDTO.getProjectId());
        if (project == null) {
            throw new RuntimeException("Project not found with ID: " + requestDTO.getProjectId());
        }

        // Validate equipment exists
        List<Equipment> equipmentList = equipmentRepository.findByIdIn(requestDTO.getEquipmentIds());
        if (equipmentList.size() != requestDTO.getEquipmentIds().size()) {
            throw new RuntimeException("One or more equipment items not found");
        }

        // Convert equipment IDs to comma-separated string for storage
        String equipmentIds = requestDTO.getEquipmentIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        EquipmentRequest request = EquipmentRequest.builder()
                .projectId(requestDTO.getProjectId())
                .siteManagerId(requestDTO.getSiteManagerId())
                .requestDate(LocalDateTime.now())
                .requestedStartDate(requestDTO.getRequestedStartDate())
                .requestedEndDate(requestDTO.getRequestedEndDate())
                .priority(requestDTO.getPriority())
                .status("PENDING")
                .additionalNotes(requestDTO.getAdditionalNotes())
                .requestPurpose(requestDTO.getRequestPurpose())
                .expectedLocation(requestDTO.getExpectedLocation())
                .equipmentIds(equipmentIds)
                .build();

        return requestRepository.save(request);
    }

    // Approve equipment request
    @Transactional
    public EquipmentRequest approveRequest(Long requestId, EquipmentStatusUpdateDTO updateDTO) {
        EquipmentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + requestId));

        // Check if equipment is available for the requested dates
        if (!isEquipmentAvailable(request)) {
            throw new RuntimeException("Requested equipment is not available for the selected dates");
        }

        request.setStatus("APPROVED");
        request.setApprovalDate(LocalDateTime.now());
        request.setApprovedBy(updateDTO.getApprovedBy());

        return requestRepository.save(request);
    }

    // Reject equipment request
    @Transactional
    public EquipmentRequest rejectRequest(Long requestId, EquipmentStatusUpdateDTO updateDTO) {
        EquipmentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + requestId));

        request.setStatus("REJECTED");
        request.setApprovalDate(LocalDateTime.now());
        request.setApprovedBy(updateDTO.getApprovedBy());
        request.setRejectionReason(updateDTO.getRejectionReason());

        return requestRepository.save(request);
    }

    // Get request by ID
    @Transactional(readOnly = true)
    public RequestResponseDTO getRequestById(Long id) {
        EquipmentRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + id));
        return convertToDTO(request);
    }

    // Helper method to check equipment availability
    private boolean isEquipmentAvailable(EquipmentRequest request) {
        // For now, return true to allow approval
        // You can implement the actual availability logic later
        return true;

        /* Uncomment and implement this later:
        List<EquipmentRequest> overlappingRequests = requestRepository.findOverlappingRequests(
                request.getRequestedStartDate(), request.getRequestedEndDate());

        return overlappingRequests.stream()
                .noneMatch(overlap -> hasEquipmentOverlap(overlap, request));
        */
    }

    private boolean hasEquipmentOverlap(EquipmentRequest existing, EquipmentRequest newRequest) {
        // Compare equipment IDs between requests
        // This is simplified - you might want more complex availability logic
        return true; // Placeholder
    }

    // Convert Entity to DTO with enriched data
    private RequestResponseDTO convertToDTO(EquipmentRequest request) {
        RequestResponseDTO dto = new RequestResponseDTO();
        dto.setId(request.getId());
        dto.setProjectId(request.getProjectId());
        dto.setSiteManagerId(request.getSiteManagerId());
        dto.setRequestDate(request.getRequestDate());
        dto.setRequestedStartDate(request.getRequestedStartDate());
        dto.setRequestedEndDate(request.getRequestedEndDate());
        dto.setPriority(request.getPriority());
        dto.setStatus(request.getStatus());
        dto.setAdditionalNotes(request.getAdditionalNotes());
        dto.setRejectionReason(request.getRejectionReason());
        dto.setApprovalDate(request.getApprovalDate());
        dto.setApprovedBy(request.getApprovedBy());
        dto.setRequestPurpose(request.getRequestPurpose());
        dto.setExpectedLocation(request.getExpectedLocation());

        // Get project name
        Project project = projectRepository.findByProjectId(request.getProjectId());
        if (project != null) {
            dto.setProjectName(project.getProjectName());
        }

        // Get equipment details
        if (request.getEquipmentIds() != null && !request.getEquipmentIds().isEmpty()) {
            List<Long> equipmentIds = List.of(request.getEquipmentIds().split(","))
                    .stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            List<Equipment> equipmentList = equipmentRepository.findByIdIn(equipmentIds);
            List<EquipmentDTO> equipmentDTOs = equipmentList.stream()
                    .map(this::convertToEquipmentDTO)
                    .collect(Collectors.toList());

            dto.setEquipmentDetails(equipmentDTOs);
        }

        return dto;
    }

    private EquipmentDTO convertToEquipmentDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        dto.setName(equipment.getName());
        dto.setType(equipment.getType());
        dto.setCategory(equipment.getCategory());
        dto.setBrand(equipment.getBrand());
        dto.setModel(equipment.getModel());
        dto.setCondition(equipment.getCondition());
        dto.setQuantity(equipment.getQuantity());
        return dto;
    }
}