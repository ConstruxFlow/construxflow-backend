package com.example.construxflow.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentRequestDTO {

    private Long id;
    private String projectId;
    private String siteManagerId;
    private LocalDateTime requestDate;
    private LocalDateTime requestedStartDate;
    private LocalDateTime requestedEndDate;
    private String priority;
    private String status;
    private String additionalNotes;
    private String rejectionReason;
    private LocalDateTime approvalDate;
    private String approvedBy;
    private List<Long> equipmentIds;  // Changed from String to List<Long>
    private String requestPurpose;
    private String expectedLocation;
    
    // Additional fields for frontend display
    private String projectName;
    private String siteManagerName;
    private List<EquipmentDTO> requestedEquipment;
}


