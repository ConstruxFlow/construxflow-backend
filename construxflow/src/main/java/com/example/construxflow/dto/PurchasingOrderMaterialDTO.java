package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasingOrderMaterialDTO {
    private Long purchasingOrderMaterialId;
    private MaterialDTO material;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal cost;
}
