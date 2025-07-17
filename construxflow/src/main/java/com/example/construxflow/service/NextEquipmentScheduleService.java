package com.example.construxflow.service;

import com.example.construxflow.dto.NextEquipmentScheduleRequestDTO;
import com.example.construxflow.dto.NextEquipmentScheduleResponseDTO;
import com.example.construxflow.entity.Next_Equipment_Schedule;
import com.example.construxflow.repository.NextEquipmentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NextEquipmentScheduleService {

    @Autowired
    private NextEquipmentScheduleRepository nextEquipmentScheduleRepository;

    public NextEquipmentScheduleResponseDTO scheduleNextEquipment(NextEquipmentScheduleRequestDTO requestDTO) {

        String nextScheduleId = "NSCH-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Next_Equipment_Schedule nextSchedule = new Next_Equipment_Schedule();
        nextSchedule.setNextScheduleId(nextScheduleId);
        nextSchedule.setAssignId(requestDTO.getAssignId());
        nextSchedule.setEquipmentScheduleId(requestDTO.getEquipmentScheduleId());
        nextSchedule.setNextMaintenanceType(requestDTO.getNextMaintenanceType());
        nextSchedule.setNextDate(requestDTO.getNextDate());
        nextSchedule.setPriority(requestDTO.getPriority());
        nextSchedule.setEstimateDuration(requestDTO.getEstimateDuration());
        nextSchedule.setTechnicianId(requestDTO.getTechnicianId());

        Next_Equipment_Schedule saved = nextEquipmentScheduleRepository.save(nextSchedule);

        NextEquipmentScheduleResponseDTO responseDTO = new NextEquipmentScheduleResponseDTO();
        responseDTO.setNextScheduleId(saved.getNextScheduleId());
        responseDTO.setEquipmentScheduleId(saved.getEquipmentScheduleId());
        responseDTO.setAssignId(saved.getAssignId());
        responseDTO.setNextMaintenanceType(saved.getNextMaintenanceType());
        responseDTO.setNextDate(saved.getNextDate());
        responseDTO.setPriority(saved.getPriority());
        responseDTO.setEstimateDuration(saved.getEstimateDuration());
        responseDTO.setTechnicianId(saved.getTechnicianId());
        return responseDTO;

    }
}
