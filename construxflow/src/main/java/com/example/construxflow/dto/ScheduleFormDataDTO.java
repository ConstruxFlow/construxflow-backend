package com.example.construxflow.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleFormDataDTO {
    private Long equipmentId;
    private String equipmentName;
    private String equipmentType;
    private String nextMaintenance;
    private String currentStatus;
}