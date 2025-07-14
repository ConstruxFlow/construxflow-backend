package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentSchedulingResponseDTO {
    private String id;
    private String equipmentType;
    private String equipmentName;
    private Date date;
    private Time time;
    private String description;
    private String status;

    // List of maintenance requests (optional - include if needed)
    private List<RequestMaintenanceMaterialsResponseDTO> maintenanceRequests;
}
