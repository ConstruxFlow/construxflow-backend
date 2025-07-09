package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
}