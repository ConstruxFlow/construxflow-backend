package com.example.construxflow.dto;

import com.example.construxflow.entity.RequestPriority;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestCreateDTO {
    private String siteName;
    private String requestedBy;
    private String requestedAt;     // "2025-08-27 10:15"
    private String itemName;
    private String itemCategory;    // "Machinery" | "Materials"
    private Integer quantity;
    private RequestPriority priority;
    private String needFrom;        // "2025-09-01"
    private String needTo;          // "2025-09-05"
    private String notes;
    private Long equipmentId;       // optional
}
