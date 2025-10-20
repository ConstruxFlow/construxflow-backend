package com.example.construxflow.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleItemDTO {
    private String scheduleId;
    private Date date;
    private String time;
    private String maintenanceType;
    private String priority;
    private String status;
    private String assignedTo;
    private String description;
}