package com.example.construxflow.dto;

import lombok.*;
import com.example.construxflow.entity.EquipmentStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentDTO {
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
}
