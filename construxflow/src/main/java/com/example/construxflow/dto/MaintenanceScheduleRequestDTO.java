package com.example.construxflow.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceScheduleRequestDTO {
    private Long equipmentId;
    private String equipmentName;
    private String equipmentType;
    private String reason;
    private String notes;
    private LocalDateTime requestedAt;
}