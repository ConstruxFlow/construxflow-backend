package com.example.construxflow.repository;

import com.example.construxflow.entity.Next_Equipment_Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NextEquipmentScheduleRepository extends JpaRepository<Next_Equipment_Schedule,String> {
}
