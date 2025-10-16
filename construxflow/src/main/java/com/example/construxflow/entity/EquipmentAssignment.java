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
@Table(name = "equipment_assignments")
public class EquipmentAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long equipmentId;              // Equipment being assigned
    private String projectId;              // Project equipment is assigned to
    private String siteManagerId;          // Site Manager responsible
    private LocalDateTime assignmentDate;  // When assignment was made
    private LocalDateTime expectedStartDate; // When equipment should start being used
    private LocalDateTime expectedEndDate;   // When equipment should be returned
    private LocalDateTime actualStartDate;   // When equipment actually started being used
    private LocalDateTime actualEndDate;     // When equipment was actually returned
    private String status;                  // Assigned, In Use, Returned, Overdue
    private String operator;                // Who is operating the equipment
    private String location;                // Current location on site
    private String notes;                   // Assignment notes
    private String returnNotes;             // Notes when returning equipment
    private Boolean isOverdue;              // Whether equipment is overdue
    private Integer overdueDays;            // Number of days overdue
}

