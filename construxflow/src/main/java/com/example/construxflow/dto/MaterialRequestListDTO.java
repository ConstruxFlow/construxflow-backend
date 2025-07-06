package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestListDTO {
    private String materialId; // Format: #MAT-001
    private String projectName;
    private String phaseName;
    private String materialName;
    private BigDecimal quantity;
    private String unitOfMeasurement;
    private String status; // NOT_REQUESTED, PENDING, APPROVED, REJECTED
}