package com.example.construxflow.dto;

import lombok.*;
import java.time.LocalDateTime;
import com.example.construxflow.entity.EquipmentStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentDTO {

    private Long id;
    private String type;
    private String name;
    private String category;
    private String brand;
    private String model;
    private String serialNumber;
    private Integer quantity;
    private String condition;
    private String purchaseDate;
    private String purchaseSource;
    private Double purchaseCost;
    private String location;
    private EquipmentStatus status;
    private String nextMaintenance;
    private String lastMaintenance;
    private String notes;
    
    // New usage tracking fields
    private String currentProjectId;
    private String currentOperator;
    private LocalDateTime currentUsageStart;
    private Double totalUsageHours;
    private Double totalKilometers;
    private LocalDateTime lastUpdated;
    private String usageStatus;
    private String assignedLocation;
    private String dailyUsageNotes;
    
    // Additional fields for frontend display
    private String projectName;
    private Boolean isAvailable;
    private String currentStatus;
    private Double todayHours;
    private Double todayKilometers;
}
