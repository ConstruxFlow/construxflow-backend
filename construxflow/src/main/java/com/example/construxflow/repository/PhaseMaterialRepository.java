package com.example.construxflow.repository;

import com.example.construxflow.entity.Phase_material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhaseMaterialRepository extends JpaRepository<Phase_material, Long> {

    @Query("SELECT pm FROM Phase_material pm " +
            "JOIN pm.project_phase pp " +
            "JOIN pp.project p " +
            "WHERE p.projectId = :projectId")
    List<Phase_material> findByProjectId(String projectId);

    @Query("SELECT pm FROM Phase_material pm " +
            "JOIN pm.project_phase pp " +
            "JOIN pp.project p")
    List<Phase_material> findAllPhaseMaterials();
}