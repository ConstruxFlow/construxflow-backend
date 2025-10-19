package com.example.construxflow.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentUsageUpdateDTO {

    private Long equipmentId;
    private String projectId;
    private String operator;
    private LocalDate usageDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double hoursUsed;
    private Double kilometersTraveled;
    private String location;
    private String purpose;
    private String notes;
    private String fuelConsumption;
    private String maintenanceNotes;
    private String weatherConditions;
    private String status;
    private String loggedBy;
    
    // Additional fields for validation
    private String equipmentName;
    private String projectName;
    private String currentStatus;
}







