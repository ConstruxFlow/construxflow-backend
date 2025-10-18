package com.example.construxflow.repository;

import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    long countByStatus(EquipmentStatus status);
    List<Equipment> findByNameContainingIgnoreCase(String name);

    @Modifying
    @Query("UPDATE Equipment e SET e.quantity = :newQuantity WHERE e.id = :id")
    void updateStockQuantity(@Param("id") Long id, @Param("newQuantity") Integer newQuantity);

    // ✅ ADD THIS: Check if equipment exists by ID
    boolean existsById(Long id);

    // ✅ ADD THIS: Delete by ID (already exists in JpaRepository, but we can add custom if needed)
    // The deleteById method is already provided by JpaRepository
}
