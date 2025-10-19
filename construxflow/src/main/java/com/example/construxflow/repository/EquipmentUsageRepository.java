package com.example.construxflow.repository;

import com.example.construxflow.entity.EquipmentUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentUsageRepository extends JpaRepository<EquipmentUsageLog, Long> {
    List<EquipmentUsageLog> findByEquipmentId(Long equipmentId);

    List<EquipmentUsageLog> findByEquipmentIdOrderByLoggedAtDesc(Long equipmentId);
}
