package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationDeliveryInfoDTO {
    private Long id;
    private LocalDate deliveryDate;
    private String location;
    private BigDecimal shippingCost;
}

