package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentAssignTechnicianRequestDTO {
    private String equipmentSchedulingId;
    private String technicianId;
    private String duration;
    private String notes;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String status;
}
