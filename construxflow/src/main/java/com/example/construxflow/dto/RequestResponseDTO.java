package com.example.construxflow.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestResponseDTO {
    private Long id;
    private String projectId;
    private String projectName;
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
    private String requestPurpose;
    private String expectedLocation;
    private List<EquipmentDTO> equipmentDetails;
}

