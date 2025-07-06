package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationReqDocDTO {
    private Long quotationReqId;
    private String documentName;
    private String documentType;
    private String filePath;
}
