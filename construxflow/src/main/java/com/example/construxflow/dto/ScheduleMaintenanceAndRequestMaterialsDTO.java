package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleMaintenanceAndRequestMaterialsDTO {

    // Equipment Scheduling fields
    private String equipmentSchedulingId; // User-provided
    private String equipmentType;
    private String equipmentName;
    private String maintenanceType;
    private String priority;
    private Date scheduleDate;
    private Time scheduleTime;
    private String scheduleNotes;
    private String status;

    // Material Request fields
    private List<MaterialItemDTO> materialItems;
    private String justification;
    private String urgencyLevel;

    // Nested DTO for material items
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MaterialItemDTO {
        private String materialRequestId; // User-provided
        private String itemId;            // User-provided
        private String itemName;
        private Double quantity;
        private String measurement;
    }
}
