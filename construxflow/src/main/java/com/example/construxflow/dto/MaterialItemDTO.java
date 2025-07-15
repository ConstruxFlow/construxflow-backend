package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialItemDTO {
    private Long materialId;
    private String materialName;
    private java.math.BigDecimal quantity;
    private String unitOfMeasurement;
} 