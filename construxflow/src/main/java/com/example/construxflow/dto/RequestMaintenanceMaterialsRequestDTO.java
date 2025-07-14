package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMaintenanceMaterialsRequestDTO {
    private String id;
    private String equipmentId; // Foreign key to Equipment_scheduling
    private String itemId;
    private String itemName;
    private Double quantity;
    private String measurement;
    private String justification;
    private String urgency;
}
