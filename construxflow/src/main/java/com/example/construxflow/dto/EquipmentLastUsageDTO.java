package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentLastUsageDTO {

    private Long equipmentId;
    private Double lastUsageHours;
    private String lastUsageLocation;
    private LocalDate lastUsageDate;
    private LocalDateTime lastLoggedAt;
    private String operator;
    private String purpose;
    private String status;

    // Additional info
    private String equipmentName;
    private boolean hasUsageHistory;
}
