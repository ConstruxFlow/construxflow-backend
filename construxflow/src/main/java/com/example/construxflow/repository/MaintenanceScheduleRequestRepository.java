package com.example.construxflow.repository;

import com.example.construxflow.entity.MaintenanceScheduleRequest;
import com.example.construxflow.entity.MaintenanceRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MaintenanceScheduleRequestRepository extends JpaRepository<MaintenanceScheduleRequest, Long> {
    List<MaintenanceScheduleRequest> findByEquipmentId(Long equipmentId);
    List<MaintenanceScheduleRequest> findByStatus(MaintenanceRequestStatus status);

    @Query("SELECT msr FROM MaintenanceScheduleRequest msr WHERE msr.equipment.id = :equipmentId AND msr.status = 'PENDING'")
    List<MaintenanceScheduleRequest> findPendingRequestsByEquipmentId(@Param("equipmentId") Long equipmentId);
}