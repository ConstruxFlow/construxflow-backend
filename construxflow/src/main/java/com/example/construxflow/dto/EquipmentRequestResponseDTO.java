package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentRequestResponseDTO {

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
    private List<Long> equipmentIds;       // List of requested equipment IDs
    private String requestPurpose;         // Purpose of equipment usage
    private String expectedLocation;       // Where equipment will be used on site

    // Enhanced field to include actual equipment details
    private List<EquipmentDTO> equipmentDetails; // Complete equipment information for each requested equipment
}
