package com.example.construxflow.repository;

import com.example.construxflow.entity.Project_phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPhaseRepository extends JpaRepository<Project_phase, Long> {
    List<Project_phase> findByProject_ProjectId(String projectId);
}