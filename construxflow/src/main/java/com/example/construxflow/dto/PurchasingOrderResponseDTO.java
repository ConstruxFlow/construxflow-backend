package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasingOrderResponseDTO {
    private Long poId;
    private String ponumber;
    private Date orderDate;
    private Date required_date;
    private String status;
    private String additionalInfo;
    private BigDecimal subTotal;
    private BigDecimal items;
    private LocalDateTime createdDate;
    private Long material_req_id;
    private String projectId;

    // Clean nested DTOs without circular references
    private SupplierDetailsDTO supplier;
    private List<PurchasingOrderMaterialDTO> materials;
    private List<PurchasingOrderDeliveryDTO> deliveries;
    private List<PurchasingOrderDocDTO> docs;
    private OrderPaymentDTO orderPayment;
}
