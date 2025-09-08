package com.example.construxflow.repository;

import com.example.construxflow.entity.EquipmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipmentScheduleRepository extends JpaRepository<EquipmentSchedule, Long> {

    // find schedules overlapping a time window (for utilization calculation)
    List<EquipmentSchedule> findByEquipmentIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
            Long equipmentId, LocalDate windowStart, LocalDate windowEnd
    );
}
