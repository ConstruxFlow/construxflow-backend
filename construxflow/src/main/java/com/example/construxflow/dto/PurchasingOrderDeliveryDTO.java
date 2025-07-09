package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasingOrderDeliveryDTO {
    private Long id;
    private LocalDate requiredDate;
    private String location;
    private BigDecimal shippingCost;
}
