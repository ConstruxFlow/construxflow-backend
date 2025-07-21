package com.example.construxflow.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.construxflow.dto.NextEquipmentScheduleResponseDTO;
import com.example.construxflow.entity.Next_Equipment_Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NextEquipmentScheduleRepository extends JpaRepository<Next_Equipment_Schedule,String> {

    List<Next_Equipment_Schedule> findByAssignId(String assignId);
}
