package com.example.construxflow.dto;

import com.example.construxflow.entity.EquipmentStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EquipmentUpsertDTO {
    private String type;
    private String name;
    private String category;
    private String brand;
    private String model;
    private String serialNumber;
    private String condition;
    private String purchaseDate;
    private String purchaseSource;
    private Double purchaseCost;
    private String location;
    private EquipmentStatus status;
    private String nextMaintenance;
    private String lastMaintenance;
    private String notes;
}
