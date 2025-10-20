package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentAverageUsageDTO {

    private Long equipmentId;
    private Double averageHoursUsed;
    private Double averageKilometersTraveled;
    private Double averageFuelConsumption;
    private Integer totalUsageRecords;

    // Additional info for display
    private String equipmentName;
    private String equipmentStatus;
}
