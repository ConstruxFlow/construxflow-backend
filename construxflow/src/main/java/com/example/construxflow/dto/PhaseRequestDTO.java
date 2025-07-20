package com.example.construxflow.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhaseRequestDTO {
    private String phaseName;
    private String startDate;
    private String endDate;
    private String status;
    private List<PhaseMaterialRequestDTO> materials;
    private BigDecimal subtotal;
}