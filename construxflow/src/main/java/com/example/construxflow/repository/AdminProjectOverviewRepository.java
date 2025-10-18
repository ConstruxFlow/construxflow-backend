// src/main/java/com/example/construxflow/repository/ProjectOverviewRepository.java
package com.example.construxflow.repository;

import com.example.construxflow.entity.Project;
import com.example.construxflow.dto.AdminProjectOverviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminProjectOverviewRepository extends JpaRepository<Project, String> {

    @Query("SELECT new com.example.construxflow.dto.AdminProjectOverviewDTO(" +
            "p.projectId, p.projectName, p.startDate, p.endDate, p.location, p.progressStatus) " +
            "FROM Project p")
    List<AdminProjectOverviewDTO> findProjectOverview();
}