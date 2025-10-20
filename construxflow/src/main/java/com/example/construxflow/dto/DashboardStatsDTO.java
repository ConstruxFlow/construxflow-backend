package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalEquipment;
    private Long availableEquipment;
    private Long scheduledEquipment;
    private Long underMaintenanceEquipment;
    private Long totalMaterials;
    private Long lowStockMaterials;
    private Long pendingRequests;
    private Long approvedRequests;
}