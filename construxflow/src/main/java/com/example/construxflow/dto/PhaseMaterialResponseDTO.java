package com.example.construxflow.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhaseMaterialResponseDTO {
    private Long phaseMaterialId;
    private Long materialId;
    private String materialName;
    private String materialType;
    private String unitOfMeasurement;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal total;
}