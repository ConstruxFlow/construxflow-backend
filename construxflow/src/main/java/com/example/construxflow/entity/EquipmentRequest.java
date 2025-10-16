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
@Table(name = "equipment_requests")
public class EquipmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectId;              // Project requesting equipment
    private String siteManagerId;          // Site Manager making the request
    private LocalDateTime requestDate;     // When request was made
    private LocalDateTime requestedStartDate; // When equipment is needed
    private LocalDateTime requestedEndDate;   // When equipment should be returned
    private String priority;               // High, Medium, Low
    private String status;                 // Pending, Approved, Rejected, In Use, Completed
    private String additionalNotes;        // Additional requirements or notes
    private String rejectionReason;        // Reason if rejected
    private LocalDateTime approvalDate;    // When approved/rejected
    private String approvedBy;             // Inventory Manager who approved/rejected
    private String equipmentIds;           // Comma-separated list of requested equipment IDs
    private String requestPurpose;         // Purpose of equipment usage
    private String expectedLocation;       // Where equipment will be used on site
}

