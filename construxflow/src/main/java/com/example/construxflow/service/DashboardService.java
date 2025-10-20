package com.example.construxflow.service;

import com.example.construxflow.dto.DashboardStatsDTO;
import com.example.construxflow.entity.EquipmentStatus;
import com.example.construxflow.repository.EquipmentRepository;
import com.example.construxflow.repository.EquipmentSchedulingRepository;
import com.example.construxflow.repository.I_MaterialRepository;
import com.example.construxflow.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EquipmentRepository equipmentRepository;
    private final I_MaterialRepository materialRepository;
    private final EquipmentSchedulingRepository equipmentSchedulingRepository;

    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // Equipment stats
        stats.setTotalEquipment(equipmentRepository.countTotalEquipment());
        stats.setAvailableEquipment(equipmentRepository.countByStatus(EquipmentStatus.AVAILABLE));
        stats.setScheduledEquipment(equipmentRepository.countByStatus(EquipmentStatus.ON_A_SITE));
        stats.setUnderMaintenanceEquipment(equipmentRepository.countByStatus(EquipmentStatus.UNDER_MAINTENANCE));

        // Material stats
        stats.setTotalMaterials(materialRepository.countTotalMaterials());
        stats.setLowStockMaterials(materialRepository.countLowStockMaterials());

        // Request stats
        stats.setPendingRequests(equipmentSchedulingRepository.countPendingRequests());
        stats.setApprovedRequests(equipmentSchedulingRepository.countApprovedRequests());

        return stats;
    }
}