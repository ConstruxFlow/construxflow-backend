
package com.example.construxflow.repository;

import com.example.construxflow.entity.EquipmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EquipmentRequestRepository extends JpaRepository<EquipmentRequest, Long> {

    // Find all requests with optional status filter
    List<EquipmentRequest> findByStatus(String status);

    // Find requests by site manager
    List<EquipmentRequest> findBySiteManagerId(String siteManagerId);

    // Find requests by project
    List<EquipmentRequest> findByProjectId(String projectId);

    // Find pending requests for dashboard counts
    long countByStatus(String status);

    // Check for overlapping equipment requests
    @Query("SELECT er FROM EquipmentRequest er WHERE er.status IN ('APPROVED', 'IN_USE') " +
            "AND er.requestedStartDate < :endDate AND er.requestedEndDate > :startDate")
    List<EquipmentRequest> findOverlappingRequests(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
}
