package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentUsageSummaryDTO {

    private Long equipmentId;
    private Double totalHoursUsed;
    private Double totalKilometersTraveled;
    private Double totalFuelConsumption;
    private Integer totalUsageRecords;

    // Additional info for display
    private String equipmentName;
    private String equipmentStatus;
}
