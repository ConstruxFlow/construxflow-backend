package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequestResponseDTO {
    private Long id;
    private String requesterName;
    private Date requestDate;
    private Date quotationDeadline;
    private String priorityLevel;
    private String status;
    private String additionalInfo;
    private String quotationType;
    private BigDecimal estimatedCost;
    private LocalDateTime createdDate;
    private Long material_req_id;
    private String projectId;

    // Clean nested DTOs without circular references
    private List<QuotationReqMaterialDTO> quotationReqMaterials;
    private List<QuotationReqDeliveryDTO> quotationReqDelivery;
    private List<QuotationReqDocDTO> quotationReqDocs;
    private String managerid;
//    private List<Material Request> material request;
}
