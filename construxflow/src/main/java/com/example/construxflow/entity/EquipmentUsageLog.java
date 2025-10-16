package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "equipment_usage_logs")
public class EquipmentUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long equipmentId;              // Equipment being logged
    private String projectId;              // Project equipment is used for
    private String operator;               // Who operated the equipment
    private LocalDate usageDate;           // Date of usage
    private LocalDateTime startTime;       // When usage started
    private LocalDateTime endTime;         // When usage ended
    private Double hoursUsed;              // Hours used today
    private Double kilometersTraveled;     // Kilometers traveled today
    private String location;               // Where equipment was used
    private String purpose;                // Purpose of usage
    private String notes;                  // Daily usage notes
    private String fuelConsumption;        // Fuel consumption if applicable
    private String maintenanceNotes;       // Any maintenance issues noticed
    private String weatherConditions;      // Weather conditions during usage
    private String status;                 // Completed, Paused, Cancelled
    private LocalDateTime loggedAt;        // When this log was created
    private String loggedBy;               // Who created this log entry
}

