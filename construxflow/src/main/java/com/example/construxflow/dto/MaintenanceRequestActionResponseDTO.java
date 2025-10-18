package com.example.construxflow.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MaintenanceRequestActionResponseDTO {
    private boolean success;
    private String message;
    private String updatedStatus;
    private Integer inventoryItemsUpdated;
}