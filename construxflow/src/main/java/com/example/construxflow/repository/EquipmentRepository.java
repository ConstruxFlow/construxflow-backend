package com.example.construxflow.repository;

import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    long countByStatus(EquipmentStatus status);
    List<Equipment> findByNameContainingIgnoreCase(String name);

}