package com.example.construxflow.service;

import com.example.construxflow.dto.MaintenanceRequestOverviewDTO;
import com.example.construxflow.entity.Equipment_scheduling;
import com.example.construxflow.dto.MaintenanceRequestDetailDTO;
import com.example.construxflow.entity.Equipment_scheduling;
import com.example.construxflow.entity.Request_manintenance_materials;

import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsDTO;
import com.example.construxflow.dto.ScheduleMaintenanceAndRequestMaterialsResponseDTO;
import com.example.construxflow.dto.EquipmentSchedulingRequestDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.dto.RequestMaintenanceMaterialsRequestDTO;
import com.example.construxflow.dto.RequestMaintenanceMaterialsResponseDTO;
import com.example.construxflow.repository.EquipmentSchedulingRepository;
import com.example.construxflow.repository.I_MaterialRepository;
import com.example.construxflow.repository.RequestMaintenanceMaterialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import com.example.construxflow.dto.*;
import com.example.construxflow.entity.I_Material;

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

    @Autowired
    private I_MaterialRepository iMaterialRepository;

    public MaintenanceRequestActionResponseDTO updateMaintenanceRequestStatus(
            MaintenanceRequestStatusUpdateDTO statusUpdateDTO) {
        try {
            // 1. Find the equipment scheduling
            Equipment_scheduling equipment = equipmentSchedulingRepository.findById(statusUpdateDTO.getEquipmentId())
                    .orElseThrow(() -> new RuntimeException("Equipment scheduling not found"));

            // 2. Update equipment status
            String oldStatus = equipment.getStatus();
            equipment.setStatus(statusUpdateDTO.getStatus());
            equipmentSchedulingRepository.save(equipment);

            // 3. Find all material requests for this equipment
            List<Request_manintenance_materials> materialRequests =
                    requestMaintenanceMaterialsRepository.findByEquipmentId(statusUpdateDTO.getEquipmentId());

            int inventoryUpdatedCount = 0;

            // 4. If approved, update inventory and material request statuses
            if ("APPROVED".equalsIgnoreCase(statusUpdateDTO.getStatus())) {
                inventoryUpdatedCount = updateInventoryForApprovedRequest(materialRequests, statusUpdateDTO);
            }

            // 5. Update material request statuses
            for (Request_manintenance_materials materialRequest : materialRequests) {
                materialRequest.setStatus(statusUpdateDTO.getStatus());
                if ("APPROVED".equalsIgnoreCase(statusUpdateDTO.getStatus()) && inventoryUpdatedCount > 0) {
                    materialRequest.setInventoryUpdated(true);
                    materialRequest.setInventoryUpdateNotes("Inventory updated upon approval");
                }
                requestMaintenanceMaterialsRepository.save(materialRequest);
            }

            return MaintenanceRequestActionResponseDTO.builder()
                    .success(true)
                    .message("Maintenance request " + statusUpdateDTO.getStatus().toLowerCase() + " successfully")
                    .updatedStatus(statusUpdateDTO.getStatus())
                    .inventoryItemsUpdated(inventoryUpdatedCount)
                    .build();

        } catch (Exception e) {
            return MaintenanceRequestActionResponseDTO.builder()
                    .success(false)
                    .message("Failed to update status: " + e.getMessage())
                    .updatedStatus(null)
                    .inventoryItemsUpdated(0)
                    .build();
        }
    }

    private int updateInventoryForApprovedRequest(
            List<Request_manintenance_materials> materialRequests,
            MaintenanceRequestStatusUpdateDTO statusUpdateDTO) {

        int updatedCount = 0;

        for (Request_manintenance_materials materialRequest : materialRequests) {
            try {
                // Find the material in inventory by name
                Optional<I_Material> inventoryMaterialOpt =
                        iMaterialRepository.findByName(materialRequest.getItemName());

                if (inventoryMaterialOpt.isPresent()) {
                    I_Material inventoryMaterial = inventoryMaterialOpt.get();

                    // Check if sufficient stock is available
                    if (inventoryMaterial.getQuantityInStock() >= materialRequest.getQuantity()) {
                        // Deduct the quantity from inventory
                        int newQuantity = inventoryMaterial.getQuantityInStock() -
                                materialRequest.getQuantity().intValue();
                        inventoryMaterial.setQuantityInStock(newQuantity);

                        iMaterialRepository.save(inventoryMaterial);
                        updatedCount++;

                        System.out.println("✅ Inventory updated for: " + materialRequest.getItemName() +
                                " | Deducted: " + materialRequest.getQuantity() +
                                " | Remaining: " + newQuantity);
                    } else {
                        System.out.println("❌ Insufficient stock for: " + materialRequest.getItemName() +
                                " | Requested: " + materialRequest.getQuantity() +
                                " | Available: " + inventoryMaterial.getQuantityInStock());
                    }
                } else {
                    System.out.println("❌ Material not found in inventory: " + materialRequest.getItemName());
                }
            } catch (Exception e) {
                System.err.println("❌ Error updating inventory for " + materialRequest.getItemName() +
                        ": " + e.getMessage());
            }
        }

        return updatedCount;
    }

    // Add method to get available materials for inventory check
    public Map<String, Object> checkInventoryAvailability(String equipmentId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> materialAvailability = new ArrayList<>();

        try {
            List<Request_manintenance_materials> materialRequests =
                    requestMaintenanceMaterialsRepository.findByEquipmentId(equipmentId);

            boolean allAvailable = true;

            for (Request_manintenance_materials materialRequest : materialRequests) {
                Map<String, Object> materialInfo = new HashMap<>();
                materialInfo.put("materialName", materialRequest.getItemName());
                materialInfo.put("requestedQuantity", materialRequest.getQuantity());

                Optional<I_Material> inventoryMaterial =
                        iMaterialRepository.findByName(materialRequest.getItemName());

                if (inventoryMaterial.isPresent()) {
                    I_Material material = inventoryMaterial.get();
                    materialInfo.put("availableQuantity", material.getQuantityInStock());
                    materialInfo.put("isAvailable", material.getQuantityInStock() >= materialRequest.getQuantity());

                    if (material.getQuantityInStock() < materialRequest.getQuantity()) {
                        allAvailable = false;
                    }
                } else {
                    materialInfo.put("availableQuantity", 0);
                    materialInfo.put("isAvailable", false);
                    allAvailable = false;
                }

                materialAvailability.add(materialInfo);
            }

            result.put("materialAvailability", materialAvailability);
            result.put("allMaterialsAvailable", allAvailable);
            result.put("success", true);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error checking inventory: " + e.getMessage());
        }

        return result;
    }

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

    public List<MaintenanceRequestOverviewDTO> getAllMaintenanceRequestsOverview() {
        List<Equipment_scheduling> schedules = equipmentSchedulingRepository.findAll();

        return schedules.stream().map(schedule -> {
            MaintenanceRequestOverviewDTO dto = new MaintenanceRequestOverviewDTO();
            dto.setEquipment(schedule.getEquipmentName());
            dto.setDate(schedule.getDate().toString()); // Or format here
            dto.setRequestedBy("System Admin"); // Placeholder or fetch from user if available
            dto.setStatus(schedule.getStatus());
            dto.setId(schedule.getId());
            return dto;
        }).collect(Collectors.toList());
    }


    public MaintenanceRequestDetailDTO getMaintenanceRequestDetails(String equipmentId) {
        Equipment_scheduling equipment = equipmentSchedulingRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        List<Request_manintenance_materials> materials =
                requestMaintenanceMaterialsRepository.findByEquipmentId(equipmentId);

        List<MaintenanceRequestDetailDTO.MaterialItem> materialItems = materials.stream().map(item -> {
            MaintenanceRequestDetailDTO.MaterialItem m = new MaintenanceRequestDetailDTO.MaterialItem();
            m.setName(item.getItemName());
            m.setDesc("Auto-generated description for " + item.getItemName()); // Replace if actual desc exists
            m.setQty(item.getQuantity().toString());
            m.setStock("N/A"); // Update if stock tracking is added
            m.setNotes(item.getJustification());
            return m;
        }).collect(Collectors.toList());

        MaintenanceRequestDetailDTO dto = new MaintenanceRequestDetailDTO();
        dto.setEquipmentName(equipment.getEquipmentName());
        dto.setRequestedBy("System Admin"); // Replace if user data exists
        dto.setSchedule(equipment.getDate() + " - " + equipment.getTime());
        dto.setPriority(equipment.getPriority() + " Priority");
        dto.setAvailability("Currently assigned to Site A - Available after 3 days");
        dto.setComments(equipment.getDescription());
        dto.setMaterials(materialItems);

        return dto;
    }

}
