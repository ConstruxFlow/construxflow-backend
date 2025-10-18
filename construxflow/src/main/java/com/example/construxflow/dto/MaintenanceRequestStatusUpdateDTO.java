package com.example.construxflow.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRequestStatusUpdateDTO {
    private String equipmentId;
    private String status; // APPROVED, REJECTED
    private String notes;
    private List<MaterialInventoryUpdateDTO> inventoryUpdates;
}
