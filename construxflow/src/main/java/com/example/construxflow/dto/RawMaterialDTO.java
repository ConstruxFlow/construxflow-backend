package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialDTO {

    private Long rawMaterialId;
    private Long materialId;
    private String materialName;
    private String materialType;
    private String unitOfMeasurement;
    private Long projectId;
    private String projectName;
    
    // Stock information
    private BigDecimal currentQuantity;
    private BigDecimal warningLevel;
    private BigDecimal criticalLevel;
    private BigDecimal urgentLevel;
    private BigDecimal reorderLevel; // Legacy field
    
    // Status information
    private String stockStatus; // NORMAL, WARNING, CRITICAL, URGENT
    private String materialStatus; // ACTIVE, INACTIVE, DISCONTINUED
    
    // Additional information
    private String lastUpdated;
    private String lastUpdatedBy;
}
