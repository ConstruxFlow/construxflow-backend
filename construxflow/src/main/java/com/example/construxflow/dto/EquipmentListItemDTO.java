package com.example.construxflow.dto;

import com.example.construxflow.entity.EquipmentStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EquipmentListItemDTO {
    private Long id;
    private String name;
    private EquipmentStatus status;   // AVAILABLE / UNDER_MAINTENANCE / ON_A_SITE
    private String displayStatus;     // "Available", "Under Maintenance", "In Use"
    private String location;
    private String lastMaintenance;
    private String utilization;       // e.g. "85%" or "N/A"
    private String buttonText;        // "Schedule" / "View Schedule" / "View Details"
    private String buttonColorClass;  // Tailwind class for quick styling
}
