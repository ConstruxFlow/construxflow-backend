package com.example.construxflow.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpcomingScheduleDTO {
    private String equipmentName;
    private String siteName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String priority;
}