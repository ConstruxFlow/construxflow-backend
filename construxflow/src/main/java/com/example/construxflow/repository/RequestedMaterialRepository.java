package com.example.construxflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.construxflow.entity.Requested_material;

public interface RequestedMaterialRepository extends JpaRepository<Requested_material, Long> {
    @Query("SELECT rm FROM Requested_material rm WHERE rm.material.materialName = :materialName AND rm.material_request.project_name = :projectName AND rm.material_request.phase_name = :phaseName")
    List<Requested_material> findByMaterialAndProjectAndPhase(
        @Param("materialName") String materialName,
        @Param("projectName") String projectName,
        @Param("phaseName") String phaseName
    );
} 