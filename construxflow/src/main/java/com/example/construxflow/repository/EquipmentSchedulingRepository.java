package com.example.construxflow.repository;

import com.example.construxflow.entity.Equipment_scheduling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Repository
public interface EquipmentSchedulingRepository extends JpaRepository<Equipment_scheduling,String> {

    // Find by equipment type
    List<Equipment_scheduling> findByEquipmentType(String equipmentType);

    // Find by equipment name
    List<Equipment_scheduling> findByEquipmentName(String equipmentName);

    // Find by date
    List<Equipment_scheduling> findByDate(Date date);

    // Find by date range
    List<Equipment_scheduling> findByDateBetween(Date startDate, Date endDate);

    // Find by equipment name containing (case-insensitive)
    List<Equipment_scheduling> findByEquipmentNameContainingIgnoreCase(String equipmentName);

    // Find by equipment type and date
    List<Equipment_scheduling> findByEquipmentTypeAndDate(String equipmentType, Date date);

    // Find by description containing
    List<Equipment_scheduling> findByDescriptionContainingIgnoreCase(String description);

    // Custom query to find equipment scheduled for today
    @Query(value = "SELECT * FROM equipment_scheduling WHERE DATE(date) = CURRENT_DATE", nativeQuery = true)
    List<Equipment_scheduling> findScheduledForToday();


    // Custom query to find equipment by time range
    @Query("SELECT e FROM Equipment_scheduling e WHERE e.time BETWEEN :startTime AND :endTime")
    List<Equipment_scheduling> findByTimeRange(@Param("startTime") Time startTime, @Param("endTime") Time endTime);

    // Custom query to find equipment with maintenance requests
    @Query("SELECT DISTINCT e FROM Equipment_scheduling e WHERE SIZE(e.maintenanceRequests) > 0")
    List<Equipment_scheduling> findEquipmentWithMaintenanceRequests();

    // Custom query to find equipment without maintenance requests
    @Query("SELECT e FROM Equipment_scheduling e WHERE SIZE(e.maintenanceRequests) = 0")
    List<Equipment_scheduling> findEquipmentWithoutMaintenanceRequests();

    // Custom query to count maintenance requests for each equipment
    @Query("SELECT e.id, e.equipmentName, SIZE(e.maintenanceRequests) FROM Equipment_scheduling e")
    List<Object[]> findEquipmentWithMaintenanceRequestCount();

    // Find equipment scheduled after a specific date
    List<Equipment_scheduling> findByDateAfter(Date date);

    // Find equipment scheduled before a specific date
    List<Equipment_scheduling> findByDateBefore(Date date);

    // Check if equipment is scheduled for a specific date and time
    boolean existsByDateAndTime(Date date, Time time);

    // Find equipment by type ordered by date
    List<Equipment_scheduling> findByEquipmentTypeOrderByDateAsc(String equipmentType);

    @Query("SELECT e.id FROM Equipment_scheduling e WHERE e.id LIKE CONCAT('EQ-', :year, '-%') ORDER BY e.id DESC LIMIT 1")
    String findLastEquipmentSchedulingIdForYear(@Param("year") int year);

    // Find by equipment ID
    List<Equipment_scheduling> findByEquipmentId(Integer equipmentId);

    Long countByStatus(String status);

    @Query("SELECT COUNT(es) FROM Equipment_scheduling es WHERE es.status = 'PENDING'")
    Long countPendingRequests();

    @Query("SELECT COUNT(es) FROM Equipment_scheduling es WHERE es.status = 'ACCEPT'")
    Long countApprovedRequests();

}
