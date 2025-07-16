package com.example.construxflow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDTO {
    private String name;
    private String category;
    private String description;
    private Integer quantityInStock;
    private String unitOfMeasure;
    private Integer reorderLevel;
    private String purchaseDate;
    private String expirationDate;
    private String supplierName;
}
