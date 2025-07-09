package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhaseMaterialRequestDTO {
    private Long materialId;
    private String materialName; // For creating new materials
    private String materialType;
    private String unitOfMeasurement;
    private BigDecimal quantity;
}