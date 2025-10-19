package com.example.construxflow.repository;

import com.example.construxflow.entity.Project_phase;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPhaseRepository extends JpaRepository<Project_phase, Long> {
    List<Project_phase> findByProject_ProjectId(String projectId);

    @Modifying
    @Transactional
    @Query("UPDATE Project_phase p SET p.status = :status WHERE p.phase_id = :phaseId")
    int updatePhaseStatus(@Param("phaseId") Long phaseId, @Param("status") String status);

    @Query("SELECT p FROM Project_phase p WHERE p.project.projectId = :projectId AND p.phase_name = :phaseName")
    Project_phase findByProjectIdAndPhaseName(@Param("projectId") String projectId, @Param("phaseName") String phaseName);
}