package com.example.construxflow.service;

import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsDTO;
import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsResponseDTO;
import com.example.construxflow.dto.EquipmentSchedulingRequestDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.dto.RequestMaintenanceMaterialsRequestDTO;
import com.example.construxflow.dto.RequestMaintenanceMaterialsResponseDTO;
import com.example.construxflow.repository.EquipmentSchedulingRepository;
import com.example.construxflow.repository.RequestMaintenanceMaterialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleMaintenanceAndRequestMaterialsService {

    @Autowired
    private EquipmentSchedulingService equipmentSchedulingService;

    @Autowired
    private RequestMaintenanceMaterialsService requestMaintenanceMaterialsService;

    // Create combined equipment scheduling and material requests
    @Autowired
    private EquipmentSchedulingRepository equipmentSchedulingRepository;

    @Autowired
    private RequestMaintenanceMaterialsRepository requestMaintenanceMaterialsRepository;

    public ScheduleMaintenanceAndRequestMaterialsResponseDTO createScheduleAndRequestMaterials(
            ScheduleMaintenanceAndRequestMaterialsDTO dto) {
        try {
            System.out.println("=== Starting Equipment Scheduling and Material Request Creation ===");
            System.out.println("Total Material Items to Process: " + (dto.getMaterialItems() != null ? dto.getMaterialItems().size() : 0));

            // Step 1: Generate unique Equipment Scheduling ID in backend
            String equipmentSchedulingId = generateUniqueEquipmentSchedulingId();
            System.out.println("Generated Equipment Scheduling ID: " + equipmentSchedulingId);

            EquipmentSchedulingRequestDTO equipmentDTO = new EquipmentSchedulingRequestDTO();
            equipmentDTO.setId(equipmentSchedulingId); // Backend-generated ID
            equipmentDTO.setEquipmentId(dto.getEquipmentId());
            equipmentDTO.setEquipmentType(dto.getEquipmentType());
            equipmentDTO.setEquipmentName(dto.getEquipmentName());
            equipmentDTO.setMaintenanceType(dto.getMaintenanceType());
            equipmentDTO.setPriority(dto.getPriority());
            equipmentDTO.setDate(dto.getScheduleDate());
            equipmentDTO.setTime(dto.getScheduleTime());
            equipmentDTO.setDescription(dto.getScheduleNotes());
            equipmentDTO.setStatus("Pending");
            equipmentDTO.setNewStatus("");

            System.out.println("Creating Equipment Scheduling...");
            EquipmentSchedulingResponseDTO equipmentResponse =
                    equipmentSchedulingService.createEquipmentScheduling(equipmentDTO);
            System.out.println("✅ Equipment Scheduling Created Successfully with ID: " + equipmentResponse.getId());

            // Step 2: Create Material Requests with backend-generated IDs
            List<RequestMaintenanceMaterialsResponseDTO> materialResponses = new ArrayList<>();

            if (dto.getMaterialItems() != null && !dto.getMaterialItems().isEmpty()) {
                System.out.println("\n=== Processing Material Items ===");

                for (int index = 0; index < dto.getMaterialItems().size(); index++) {
                    ScheduleMaintenanceAndRequestMaterialsDTO.MaterialItemDTO item = dto.getMaterialItems().get(index);

                    System.out.println("\n--- Processing Item " + (index + 1) + " ---");
                    System.out.println("Item Name: " + item.getItemName());
                    System.out.println("Quantity: " + item.getQuantity());

                    if (item.getItemName() != null && !item.getItemName().trim().isEmpty()) {

                        try {
                            // Generate unique IDs in backend
                            String materialRequestId = generateUniqueMaterialRequestId();
                            String itemId = generateUniqueItemId();

                            System.out.println("Generated Material Request ID: " + materialRequestId);
                            System.out.println("Generated Item ID: " + itemId);

                            RequestMaintenanceMaterialsRequestDTO materialDTO = new RequestMaintenanceMaterialsRequestDTO();
                            materialDTO.setId(materialRequestId); // Backend-generated ID
                            materialDTO.setEquipmentId(equipmentResponse.getId());
                            materialDTO.setItemId(itemId); // Backend-generated ID
                            materialDTO.setItemName(item.getItemName());
                            materialDTO.setQuantity(item.getQuantity().doubleValue());
                            materialDTO.setMeasurement(item.getMeasurement());
                            materialDTO.setJustification(dto.getJustification());
                            materialDTO.setUrgency(dto.getUrgencyLevel() != null ?
                                    dto.getUrgencyLevel().toUpperCase() : "MEDIUM");

                            System.out.println("Attempting to save Material Request: " + materialRequestId);
                            System.out.println(materialDTO.getMeasurement());

                            RequestMaintenanceMaterialsResponseDTO materialResponse =
                                    requestMaintenanceMaterialsService.createMaintenanceRequest(materialDTO);

                            materialResponses.add(materialResponse);
                            System.out.println("✅ Material Request Saved Successfully: " + materialResponse.getId());

                        } catch (Exception materialException) {
                            System.err.println("❌ Error saving Material Request for item " + item.getItemName() + ": " + materialException.getMessage());
                            materialException.printStackTrace();

                            // Re-throw to stop processing and rollback
                            throw new RuntimeException("Failed to save material request for item " + item.getItemName() + ": " + materialException.getMessage(), materialException);
                        }
                    } else {
                        System.out.println("⚠️ Skipping item with empty name");
                    }
                }
            }

            System.out.println("\n=== Summary ===");
            System.out.println("Equipment Scheduling Created: " + equipmentResponse.getId());
            System.out.println("Material Requests Created: " + materialResponses.size());
            System.out.println("Expected Material Requests: " + (dto.getMaterialItems() != null ? dto.getMaterialItems().size() : 0));

            if (materialResponses.size() != dto.getMaterialItems().size()) {
                System.err.println("⚠️ WARNING: Not all material requests were created!");
            }

            return ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                    .equipmentScheduling(equipmentResponse)
                    .materialRequests(materialResponses)
                    .message("Equipment scheduling and material requests created successfully. Created " +
                            materialResponses.size() + " material requests.")
                    .success(true)
                    .build();

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation Error: " + e.getMessage());
            throw new RuntimeException("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create equipment scheduling and material requests: " + e.getMessage());
        }
    }

    // Helper method to generate unique Equipment Scheduling ID
    private String generateUniqueEquipmentSchedulingId() {
        try {
            // Get the current year
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            String yearPrefix = "EQ-" + currentYear + "-";

            // Find the last used equipment scheduling ID for this year
            String lastId = equipmentSchedulingRepository.findLastEquipmentSchedulingIdForYear(currentYear);

            int nextNumber = 1;
            if (lastId != null && !lastId.isEmpty()) {
                // Extract the number part and increment
                String numberPart = lastId.substring(lastId.lastIndexOf("-") + 1);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return yearPrefix + String.format("%03d", nextNumber);
        } catch (Exception e) {
            // Fallback to timestamp-based ID if database query fails
            return "EQSCH-" + Calendar.getInstance().get(Calendar.YEAR) + "-" + System.currentTimeMillis() % 1000;
        }
    }

    // Helper method to generate unique Material Request ID
    private String generateUniqueMaterialRequestId() {
        try {
            // Find the last used material request ID
            String lastId = requestMaintenanceMaterialsRepository.findLastMaterialRequestId();

            int nextNumber = 1;
            if (lastId != null && !lastId.isEmpty()) {
                // Extract the number part and increment
                String numberPart = lastId.substring(lastId.lastIndexOf("-") + 1);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return "MAT-REQ-" + String.format("%03d", nextNumber);
        } catch (Exception e) {
            // Fallback to timestamp-based ID if database query fails
            return "MAT-REQ-" + System.currentTimeMillis() % 10000;
        }
    }

    // Helper method to generate unique Item ID
    private String generateUniqueItemId() {
        try {
            // Find the last used item ID
            String lastId = requestMaintenanceMaterialsRepository.findLastItemId();

            int nextNumber = 1;
            if (lastId != null && !lastId.isEmpty()) {
                // Extract the number part and increment
                String numberPart = lastId.substring(lastId.lastIndexOf("-") + 1);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return "ITEM-" + String.format("%03d", nextNumber);
        } catch (Exception e) {
            // Fallback to timestamp-based ID if database query fails
            return "ITEM-" + System.currentTimeMillis() % 10000;
        }
    }



    // Update combined data with user-provided IDs
    public ScheduleMaintenanceAndRequestMaterialsResponseDTO updateScheduleAndRequestMaterials(
            String equipmentId, ScheduleMaintenanceAndRequestMaterialsDTO dto) {

        try {
            // Update Equipment Scheduling
            EquipmentSchedulingRequestDTO equipmentDTO = new EquipmentSchedulingRequestDTO();
            equipmentDTO.setEquipmentType(dto.getEquipmentType());
            equipmentDTO.setEquipmentName(dto.getEquipmentName());
            equipmentDTO.setMaintenanceType(dto.getMaintenanceType());
            equipmentDTO.setPriority(dto.getPriority());
            equipmentDTO.setDate(dto.getScheduleDate());
            equipmentDTO.setTime(dto.getScheduleTime());
            equipmentDTO.setDescription(dto.getScheduleNotes());

            EquipmentSchedulingResponseDTO equipmentResponse =
                    equipmentSchedulingService.updateEquipmentScheduling(equipmentId, equipmentDTO);

            // Delete existing material requests for this equipment
            List<RequestMaintenanceMaterialsResponseDTO> existingRequests =
                    requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentId(equipmentId);

            for (RequestMaintenanceMaterialsResponseDTO existingRequest : existingRequests) {
                requestMaintenanceMaterialsService.deleteMaintenanceRequest(existingRequest.getId());
            }

            // Create new material requests with user-provided IDs
            List<RequestMaintenanceMaterialsResponseDTO> materialResponses = new ArrayList<>();

            if (dto.getMaterialItems() != null && !dto.getMaterialItems().isEmpty()) {
                for (ScheduleMaintenanceAndRequestMaterialsDTO.MaterialItemDTO item : dto.getMaterialItems()) {
                    if (item.getItemName() != null && !item.getItemName().trim().isEmpty()) {

                        // Validate required user-provided IDs
                        if (item.getMaterialRequestId() == null || item.getMaterialRequestId().trim().isEmpty()) {
                            throw new IllegalArgumentException("Material Request ID is required for item: " + item.getItemName());
                        }
                        if (item.getItemId() == null || item.getItemId().trim().isEmpty()) {
                            throw new IllegalArgumentException("Item ID is required for item: " + item.getItemName());
                        }

                        RequestMaintenanceMaterialsRequestDTO materialDTO = new RequestMaintenanceMaterialsRequestDTO();
                        materialDTO.setId(item.getMaterialRequestId()); // User-provided ID
                        materialDTO.setEquipmentId(equipmentId);
                        materialDTO.setItemId(item.getItemId()); // User-provided ID
                        materialDTO.setItemName(item.getItemName());
                        materialDTO.setQuantity(item.getQuantity().doubleValue());
                        materialDTO.setJustification(dto.getJustification());
                        materialDTO.setUrgency(dto.getUrgencyLevel() != null ?
                                dto.getUrgencyLevel().toUpperCase() : "MEDIUM");

                        RequestMaintenanceMaterialsResponseDTO materialResponse =
                                requestMaintenanceMaterialsService.createMaintenanceRequest(materialDTO);
                        materialResponses.add(materialResponse);
                    }
                }
            }

            return ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                    .equipmentScheduling(equipmentResponse)
                    .materialRequests(materialResponses)
                    .message("Equipment scheduling and material requests updated successfully")
                    .success(true)
                    .build();

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update equipment scheduling and material requests: " + e.getMessage());
        }
    }

    // Other methods remain the same...
    public ScheduleMaintenanceAndRequestMaterialsResponseDTO getScheduleAndMaterialsByEquipmentId(String equipmentId) {
        try {
            Optional<EquipmentSchedulingResponseDTO> equipmentResponse =
                    equipmentSchedulingService.getEquipmentSchedulingById(equipmentId);

            if (equipmentResponse.isEmpty()) {
                return ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                        .message("Equipment scheduling not found")
                        .success(false)
                        .build();
            }

            List<RequestMaintenanceMaterialsResponseDTO> materialRequests =
                    requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentId(equipmentId);

            return ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                    .equipmentScheduling(equipmentResponse.get())
                    .materialRequests(materialRequests)
                    .message("Data retrieved successfully")
                    .success(true)
                    .build();

        } catch (Exception e) {
            return ScheduleMaintenanceAndRequestMaterialsResponseDTO.builder()
                    .message("Failed to retrieve data: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }

    public void deleteScheduleAndRequestMaterials(String equipmentId) {
        try {
            // Delete material requests first (due to foreign key constraint)
            List<RequestMaintenanceMaterialsResponseDTO> materialRequests =
                    requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentId(equipmentId);

            for (RequestMaintenanceMaterialsResponseDTO request : materialRequests) {
                requestMaintenanceMaterialsService.deleteMaintenanceRequest(request.getId());
            }

            // Delete equipment scheduling
            equipmentSchedulingService.deleteEquipmentScheduling(equipmentId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete equipment scheduling and material requests: " + e.getMessage());
        }
    }





    // Check if equipment has pending material requests
    public boolean hasEquipmentPendingMaterialRequests(String equipmentId) {
        try {
            List<RequestMaintenanceMaterialsResponseDTO> requests =
                    requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentId(equipmentId);
            return !requests.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if equipment has high priority pending material requests
    public boolean hasEquipmentHighPriorityPendingRequests(String equipmentId) {
        try {
            List<RequestMaintenanceMaterialsResponseDTO> allRequests =
                    requestMaintenanceMaterialsService.getMaintenanceRequestsByEquipmentId(equipmentId);

            return allRequests.stream()
                    .anyMatch(request -> "HIGH".equalsIgnoreCase(request.getUrgency()) ||
                            "CRITICAL".equalsIgnoreCase(request.getUrgency()));
        } catch (Exception e) {
            return false;
        }
    }

    // Get count of pending material requests for equipment
    public long countEquipmentPendingMaterialRequests(String equipmentId) {
        try {
            return requestMaintenanceMaterialsService.countMaintenanceRequestsByEquipmentId(equipmentId);
        } catch (Exception e) {
            return 0;
        }
    }



}
