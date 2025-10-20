package com.example.construxflow.dto;

import lombok.Data;

@Data
public class LowStockItemDTO {
    private String name;
    private String category;
    private Integer currentStock;
    private Integer reorderLevel;
    private String urgency;
}