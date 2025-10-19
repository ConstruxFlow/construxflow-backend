package com.example.construxflow.dto;

import com.example.construxflow.entity.EquipmentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminEquipmentSummaryDTO {
    private Long id;
    private String name;
    private EquipmentStatus status;
    private String lastMaintenance; // ISO string (from entity's toString)
}