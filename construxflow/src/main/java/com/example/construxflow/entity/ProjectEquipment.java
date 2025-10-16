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
@Table(name = "project_equipment")
public class ProjectEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectId;              // Project ID
    private Long equipmentId;              // Equipment ID
    private String status;                 // Pending, In Use, Returned, Maintenance
    private LocalDateTime assignedDate;    // When equipment was assigned to project
    private LocalDateTime startDate;       // When equipment usage started
    private LocalDateTime endDate;         // When equipment usage ended
    private String assignedBy;             // Who assigned the equipment
    private String operator;               // Current operator
    private String location;               // Current location on project site
    private String notes;                  // Assignment notes
    private Double totalHoursUsed;         // Total hours used for this project
    private Double totalKilometers;        // Total kilometers for this project
    private String returnReason;           // Reason for return
    private LocalDateTime lastUpdated;     // Last update timestamp
}


