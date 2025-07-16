package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationReqDeliveryDTO {
    private Long quotationReqDeliveryId;
    private String location;
    private String deliveryDate;
    private BigDecimal quantitySplit;
}
