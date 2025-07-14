package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItemDTO {
    private Long id;
    private MaterialDTO material;
    private int quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
