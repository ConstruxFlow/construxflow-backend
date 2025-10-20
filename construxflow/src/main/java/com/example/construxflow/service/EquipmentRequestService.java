
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

    @Autowired
    private EquipmentRequestRepository equipmentRequestRepository;

    @Autowired
    private EquipmentService equipmentService;

    public EquipmentRequestResponseDTO createEquipmentRequest(EquipmentRequestDTO requestDTO) {
        EquipmentRequest equipmentRequest = new EquipmentRequest();

        // Map DTO to Entity
        equipmentRequest.setProjectId(requestDTO.getProjectId());
        equipmentRequest.setSiteManagerId(requestDTO.getSiteManagerId());
        equipmentRequest.setRequestDate(requestDTO.getRequestDate() != null ?
            requestDTO.getRequestDate() : LocalDateTime.now());
        equipmentRequest.setRequestedStartDate(requestDTO.getRequestedStartDate());
        equipmentRequest.setRequestedEndDate(requestDTO.getRequestedEndDate());
        equipmentRequest.setPriority(requestDTO.getPriority());
        equipmentRequest.setStatus(requestDTO.getStatus() != null ?
            requestDTO.getStatus() : "Pending");
        equipmentRequest.setAdditionalNotes(requestDTO.getAdditionalNotes());
        equipmentRequest.setRejectionReason(requestDTO.getRejectionReason());
        equipmentRequest.setApprovalDate(requestDTO.getApprovalDate());
        equipmentRequest.setApprovedBy(requestDTO.getApprovedBy());

        // Convert List<Long> to comma-separated String for entity
        if (requestDTO.getEquipmentIds() != null && !requestDTO.getEquipmentIds().isEmpty()) {
            String equipmentIdsString = requestDTO.getEquipmentIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
            equipmentRequest.setEquipmentIds(equipmentIdsString);
        }

        equipmentRequest.setRequestPurpose(requestDTO.getRequestPurpose());
        equipmentRequest.setExpectedLocation(requestDTO.getExpectedLocation());

        // Save the entity
        EquipmentRequest savedRequest = equipmentRequestRepository.save(equipmentRequest);

        // Convert comma-separated String back to List<Long> for response
        List<Long> equipmentIdsList = null;
        if (savedRequest.getEquipmentIds() != null && !savedRequest.getEquipmentIds().isEmpty()) {
            equipmentIdsList = Arrays.stream(savedRequest.getEquipmentIds().split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        }

        // Fetch equipment details
        List<EquipmentDTO> equipmentDetails = fetchEquipmentDetails(equipmentIdsList);

        // Map saved entity to response DTO
        return EquipmentRequestResponseDTO.builder()
            .id(savedRequest.getId())
            .projectId(savedRequest.getProjectId())
            .siteManagerId(savedRequest.getSiteManagerId())
            .requestDate(savedRequest.getRequestDate())
            .requestedStartDate(savedRequest.getRequestedStartDate())
            .requestedEndDate(savedRequest.getRequestedEndDate())
            .priority(savedRequest.getPriority())
            .status(savedRequest.getStatus())
            .additionalNotes(savedRequest.getAdditionalNotes())
            .rejectionReason(savedRequest.getRejectionReason())
            .approvalDate(savedRequest.getApprovalDate())
            .approvedBy(savedRequest.getApprovedBy())
            .equipmentIds(equipmentIdsList)
            .requestPurpose(savedRequest.getRequestPurpose())
            .expectedLocation(savedRequest.getExpectedLocation())
            .equipmentDetails(equipmentDetails)
            .build();
    }

    public List<EquipmentRequestResponseDTO> getAllEquipmentRequests(){
        List<EquipmentRequest> requests = equipmentRequestRepository.findAll();

        return requests.stream()
            .map(saved -> {
                // Convert comma-separated String to List<Long> for equipmentIds
                List<Long> equipmentIdsList = null;
                if (saved.getEquipmentIds() != null && !saved.getEquipmentIds().isEmpty()) {
                    equipmentIdsList = Arrays.stream(saved.getEquipmentIds().split(","))
                        .map(String::trim)
                        .map(Long::valueOf)
                        .collect(Collectors.toList());
                }

                // Fetch equipment details for this request
                List<EquipmentDTO> equipmentDetails = fetchEquipmentDetails(equipmentIdsList);

                return EquipmentRequestResponseDTO.builder()
                    .id(saved.getId())
                    .projectId(saved.getProjectId())
                    .siteManagerId(saved.getSiteManagerId())
                    .requestDate(saved.getRequestDate())
                    .requestedStartDate(saved.getRequestedStartDate())
                    .requestedEndDate(saved.getRequestedEndDate())
                    .priority(saved.getPriority())
                    .status(saved.getStatus())
                    .additionalNotes(saved.getAdditionalNotes())
                    .rejectionReason(saved.getRejectionReason())
                    .approvalDate(saved.getApprovalDate())
                    .approvedBy(saved.getApprovedBy())
                    .equipmentIds(equipmentIdsList)
                    .requestPurpose(saved.getRequestPurpose())
                    .expectedLocation(saved.getExpectedLocation())
                    .equipmentDetails(equipmentDetails)
                    .build();
            })
            .collect(Collectors.toList());
    }

    private List<EquipmentDTO> fetchEquipmentDetails(List<Long> equipmentIds) {
        if (equipmentIds == null || equipmentIds.isEmpty()) {
            return List.of();
        }

        List<Equipment> equipmentList = equipmentService.getEquipmentByIds(equipmentIds);
        return equipmentList.stream()
            .map(this::convertToEquipmentDTO)
            .collect(Collectors.toList());
    }

    private EquipmentDTO convertToEquipmentDTO(Equipment equipment) {
        return EquipmentDTO.builder()
            .id(equipment.getId())
            .type(equipment.getType())
            .name(equipment.getName())
            .category(equipment.getCategory())
            .brand(equipment.getBrand())
            .model(equipment.getModel())
            .serialNumber(equipment.getSerialNumber())
            .quantity(equipment.getQuantity())
            .condition(equipment.getCondition())
            .purchaseDate(equipment.getPurchaseDate())
            .purchaseSource(equipment.getPurchaseSource())
            .purchaseCost(equipment.getPurchaseCost())
            .location(equipment.getLocation())
            .status(equipment.getStatus())
            .nextMaintenance(equipment.getNextMaintenance())
            .lastMaintenance(equipment.getLastMaintenance())
            .notes(equipment.getNotes())
            .build();
    }

    public EquipmentRequestResponseDTO updateEquipmentRequestStatus(Long requestId, String newStatus) {
        // Find the existing equipment request
        EquipmentRequest existingRequest = equipmentRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Equipment request not found with id: " + requestId));

        // Update only the status
        existingRequest.setStatus(newStatus);

        // Save the updated request
        EquipmentRequest updatedRequest = equipmentRequestRepository.save(existingRequest);

        // Convert comma-separated String back to List<Long> for response
        List<Long> equipmentIdsList = null;
        if (updatedRequest.getEquipmentIds() != null && !updatedRequest.getEquipmentIds().isEmpty()) {
            equipmentIdsList = Arrays.stream(updatedRequest.getEquipmentIds().split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        }

        // Fetch equipment details
        List<EquipmentDTO> equipmentDetails = fetchEquipmentDetails(equipmentIdsList);

        // Return updated response DTO
        return EquipmentRequestResponseDTO.builder()
            .id(updatedRequest.getId())
            .projectId(updatedRequest.getProjectId())
            .siteManagerId(updatedRequest.getSiteManagerId())
            .requestDate(updatedRequest.getRequestDate())
            .requestedStartDate(updatedRequest.getRequestedStartDate())
            .requestedEndDate(updatedRequest.getRequestedEndDate())
            .priority(updatedRequest.getPriority())
            .status(updatedRequest.getStatus())
            .additionalNotes(updatedRequest.getAdditionalNotes())
            .rejectionReason(updatedRequest.getRejectionReason())
            .approvalDate(updatedRequest.getApprovalDate())
            .approvedBy(updatedRequest.getApprovedBy())
            .equipmentIds(equipmentIdsList)
            .requestPurpose(updatedRequest.getRequestPurpose())
            .expectedLocation(updatedRequest.getExpectedLocation())
            .equipmentDetails(equipmentDetails)
            .build();
    }
