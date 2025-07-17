package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentSchedulingRequestDTO {
    private String id;
    private String equipmentType;
    private String equipmentName;
    private String maintenanceType;
    private String priority;
    private Date date;
    private Time time;
    private String description;
    private String status;
    private String newStatus;
}
