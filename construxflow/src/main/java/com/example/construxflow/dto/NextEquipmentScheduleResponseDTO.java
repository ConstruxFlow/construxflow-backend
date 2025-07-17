package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextEquipmentScheduleResponseDTO {

    private String nextScheduleId;
    private String assignId;
    private String equipmentScheduleId;
    private String nextMaintenanceType;
    private String nextDate;
    private String estimateDuration;
    private String priority;
    private String technicianId;
}
