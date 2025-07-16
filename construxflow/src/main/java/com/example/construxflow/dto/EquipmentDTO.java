package com.example.construxflow.dto;

import lombok.*;

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
    private String condition;
    private String purchaseDate;
    private String purchaseSource;
    private Double purchaseCost;
    private String location;
    private String status;
    private String nextMaintenance;
    private String notes;
}
