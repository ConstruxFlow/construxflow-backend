package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentAssignTechnicianRequestDTO {
    private String equipmentId;
    private String equipmentSchedulingId;
    private List<String> technicianIds;
    private String duration;
    private String notes;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String status;
}
