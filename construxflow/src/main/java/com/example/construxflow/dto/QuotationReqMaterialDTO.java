package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationReqMaterialDTO {
    private Long quotationReqMaterialId;
    private MaterialDTO material;
    private BigDecimal quantity;
}
