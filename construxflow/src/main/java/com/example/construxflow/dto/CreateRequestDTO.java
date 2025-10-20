package com.example.construxflow.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateRequestDTO {
    private String projectId;
    private String siteManagerId;
    private LocalDateTime requestedStartDate;
    private LocalDateTime requestedEndDate;
    private String priority;
    private String additionalNotes;
    private String requestPurpose;
    private String expectedLocation;
    private List<Long> equipmentIds;
}