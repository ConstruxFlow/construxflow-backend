package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhaseResponseDTO {
    private Long phaseId;
    private String phaseName;
    private String startDate;
    private String endDate;
    private String status;
    private List<PhaseMaterialResponseDTO> materials;
}