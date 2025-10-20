
package com.example.construxflow.repository;

import com.example.construxflow.entity.EquipmentSchedule;
import com.example.construxflow.entity.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EquipmentScheduleRepository extends JpaRepository<EquipmentSchedule, Long> {

    // Find active schedules for equipment
    @Query("SELECT es FROM EquipmentSchedule es WHERE es.equipment.id = :equipmentId AND es.status = 'SCHEDULED' AND es.endDate >= :today")
    List<EquipmentSchedule> findActiveSchedulesByEquipmentId(@Param("equipmentId") Long equipmentId, @Param("today") LocalDate today);

    // Check for scheduling conflicts
    @Query("SELECT COUNT(es) > 0 FROM EquipmentSchedule es WHERE es.equipment.id = :equipmentId AND es.status = 'SCHEDULED' AND ((es.startDate BETWEEN :startDate AND :endDate) OR (es.endDate BETWEEN :startDate AND :endDate) OR (es.startDate <= :startDate AND es.endDate >= :endDate))")
    boolean hasSchedulingConflict(@Param("equipmentId") Long equipmentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<EquipmentSchedule> findByEquipmentId(Long equipmentId);

    List<EquipmentSchedule> findByStartDateBetween(LocalDate start, LocalDate end);
    List<EquipmentSchedule> findByStatus(ScheduleStatus status);

    @Query("SELECT es FROM EquipmentSchedule es WHERE es.startDate >= :today ORDER BY es.startDate ASC")
    List<EquipmentSchedule> findUpcomingSchedules(LocalDate today);

}