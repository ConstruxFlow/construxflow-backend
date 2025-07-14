package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationAttachmentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private String fileUrl;
}


