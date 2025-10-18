package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInventorySummaryDTO {
    private long totalQuantityInStock; // SUM(quantityInStock)
    private long uniqueCategories;     // COUNT(DISTINCT category)
}