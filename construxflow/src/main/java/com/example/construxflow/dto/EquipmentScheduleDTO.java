package com.example.construxflow.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentScheduleDTO {
    private Long equipmentId;
    private String siteName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;

    // For form pre-fill
    private String equipmentName;
    private String equipmentType;
    private String nextMaintenance;

    private List<ScheduleItemDTO> scheduleItems;
    private List<MaintenanceHistoryDTO> maintenanceHistory;
}