package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    private String nextMaintenance;
    private String lastMaintenance;
    private String notes;

    private Double hoursUsed;
    private Double kilometersTraveled;
    private String fuelConsumption;
}

