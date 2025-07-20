package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRequestOverviewDTO {
    private String id;

    private String equipment;
    private String date;
    private String requestedBy;
    private String status;
}
