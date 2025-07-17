package com.example.construxflow.repository;

import com.example.construxflow.entity.Equipment_Assign_Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentAssignTechnicianRepository extends JpaRepository<Equipment_Assign_Technician,String> {

    Optional<Equipment_Assign_Technician> findByEquipmentSchedulingId(String equipmentSchedulingId);
}
