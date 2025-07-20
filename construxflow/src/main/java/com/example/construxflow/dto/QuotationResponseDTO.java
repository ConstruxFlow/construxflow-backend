package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationResponseDTO {
    private Long id;
    private Long quotationRequestId;
    private String supplierId;
    private LocalDateTime createdAt;
    private BigDecimal advancedPayment;
    private String paymentTerms;
    private String notes;
    private BigDecimal totalAmount;
    private String status;
    private List<QuotationItemDTO> items;
    private List<QuotationDeliveryInfoDTO> deliveryInfos;
    private List<QuotationAttachmentDTO> attachments;
}

