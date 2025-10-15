package com.example.construxflow.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInventoryOverviewDTO {
    private List<AdminInventoryOverviewDTO.StockItemDTO> stockItems;     // Category-level cards
    private List<AdminInventoryOverviewDTO.CriticalItemDTO> criticalItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockItemDTO {
        private String category;
        private Long totalQuantity;        // Sum of quantityInStock within the category
        private Long totalReorderLevel;    // Sum of reorderLevel within the category
        private Long recommendedLevel;     // 2x totalReorderLevel (target)
        private Integer capacityPercent;   // % of recommendedLevel (clamped 0..100)
        private String status;             // "Good" | "Low" | "Critical"
        private Long materialsCount;       // Number of distinct materials in the category
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriticalItemDTO {
        private Long id;
        private String name;
        private String category;
        private Integer current;           // quantityInStock
        private Integer min;               // reorderLevel
        private String unitOfMeasure;
        private String priority;           // "high" | "medium"
    }
}