package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private String condition;
    private String purchaseDate;
    private String purchaseSource;
    private Double purchaseCost;
    private String location;
    private String status;
    private String nextMaintenance;
    private String notes;

    // New fields for site inventory management
    private String currentProjectId;        // Which project is currently using this equipment
    private String currentOperator;         // Who is currently operating this equipment
    private LocalDateTime currentUsageStart; // When the current usage started
    private Double totalUsageHours;         // Cumulative total hours used
    private Double totalKilometers;         // Cumulative total kilometers traveled
    private LocalDateTime lastUpdated;      // Last time equipment status was updated
    private String usageStatus;             // Available, In Use, Maintenance, Returned, Pending
    private String assignedLocation;        // Current location on the project site
    private String dailyUsageNotes;         // Daily usage notes and observations
}
