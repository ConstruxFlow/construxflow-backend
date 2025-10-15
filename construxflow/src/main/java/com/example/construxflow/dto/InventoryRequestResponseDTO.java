package com.example.construxflow.dto;

import com.example.construxflow.entity.RequestPriority;
import com.example.construxflow.entity.RequestStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestResponseDTO {
    private Long id;
    private String siteName;
    private String requestedBy;
    private String requestedAt;
    private String itemName;
    private String itemCategory;
    private Integer quantity;
    private RequestPriority priority;
    private RequestStatus status;
    private String needFrom;
    private String needTo;
    private String notes;
    private Long equipmentId;
}
