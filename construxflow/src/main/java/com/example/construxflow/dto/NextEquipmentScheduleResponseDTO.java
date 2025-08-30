package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NextEquipmentScheduleResponseDTO {

    private String nextScheduleId;
    private String assignId;
    private String equipmentId;
    private String equipmentScheduleId;
    private String nextMaintenanceType;
    private String nextDate;
    private String estimateDuration;
    private String priority;
    private String technicianId;
    private String lastMaintenanceDate;
}
