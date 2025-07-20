package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {
    private String projectId;
    private String managerId;
    private String projectName;
    private String location;
    private String startDate;
    private String endDate;
    private String progressStatus;
    private List<PhaseResponseDTO> phases;
    private List<String> documentPaths;
}