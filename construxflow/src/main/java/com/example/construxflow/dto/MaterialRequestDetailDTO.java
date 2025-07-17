package com.example.construxflow.dto;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestDetailDTO {
    private Long requestId;
    private String status;
    private String additionalInfo;
    private String priority;
    private Date requestDate;
    private String projectName;
    private String phaseName;
    private String projectId;
    private List<RequestedMaterialDTO> requestedMaterials;
}
