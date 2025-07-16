package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleMaintenanceAndRequestMaterialsResponseDTO {

    private EquipmentSchedulingResponseDTO equipmentScheduling;
    private List<RequestMaintenanceMaterialsResponseDTO> materialRequests;
    private String message;
    private boolean success;
}
