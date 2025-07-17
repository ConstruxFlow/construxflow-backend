package com.example.construxflow.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestedMaterialDTO {
    private Long requestedMaterialId;
    private String materialName;
    private String materialType;
    private BigDecimal quantity;
    private String unitOfMeasurement;
    private String status;
    private String unitPrice;
}
