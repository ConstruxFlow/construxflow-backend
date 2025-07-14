package com.example.construxflow.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private java.math.BigDecimal subtotal;
}