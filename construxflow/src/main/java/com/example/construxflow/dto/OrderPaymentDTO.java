package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentDTO {
    private Long paymentId;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private String paymentType;
    private String status;
    private String referenceNumber;
    private String notes;
    private String bankDetails;
    private LocalDateTime paymentDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
