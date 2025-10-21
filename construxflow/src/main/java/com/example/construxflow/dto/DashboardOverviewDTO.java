// DashboardOverviewDTO.java
package com.example.construxflow.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardOverviewDTO {
    private Long totalEquipment;
    private Long availableEquipment;
    private Long scheduledEquipment;
    private Long underMaintenanceEquipment;
    private Long totalMaterials;
    private Long lowStockMaterials;
    private Long pendingRequests;
    private Long approvedRequests;
    private List<EquipmentStatusDTO> equipmentStatus;
    private List<RecentActivityDTO> recentActivities;
    private List<LowStockItemDTO> lowStockItems;
    private List<UpcomingScheduleDTO> upcomingSchedules;
}
