package com.example.construxflow.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceHistoryDTO {
    private String maintenanceId;
    private Date date;
    private String type;
    private String performedBy;
    private String description;
    private String status;
    private Double cost;
}