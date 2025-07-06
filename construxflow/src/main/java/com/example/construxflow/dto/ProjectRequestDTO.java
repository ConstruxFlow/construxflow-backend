package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDTO {
    private String projectName;
    private String location;
    private String startDate;
    private String endDate;
    private String progressStatus;
    private MultipartFile boqFile; // For BOQ upload
    private List<PhaseRequestDTO> phases;
}