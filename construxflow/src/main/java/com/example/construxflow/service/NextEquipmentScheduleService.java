package com.example.construxflow.service;

import com.example.construxflow.dto.NextEquipmentScheduleRequestDTO;
import com.example.construxflow.dto.NextEquipmentScheduleResponseDTO;
import com.example.construxflow.entity.Next_Equipment_Schedule;
import com.example.construxflow.repository.NextEquipmentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NextEquipmentScheduleService {

    @Autowired
    private NextEquipmentScheduleRepository nextEquipmentScheduleRepository;

    public List<NextEquipmentScheduleResponseDTO> scheduleNextEquipment(NextEquipmentScheduleRequestDTO requestDTO) {
        List<NextEquipmentScheduleResponseDTO> responseList = new ArrayList<>();

        for (String technicianId : requestDTO.getTechnicianIds()) {
            String nextScheduleId = "NSCH-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

            Next_Equipment_Schedule schedule = new Next_Equipment_Schedule();
            schedule.setNextScheduleId(nextScheduleId);
            schedule.setAssignId(requestDTO.getAssignId());
            schedule.setEquipmentId(requestDTO.getEquipmentId());
            schedule.setEquipmentScheduleId(requestDTO.getEquipmentScheduleId());
            schedule.setNextMaintenanceType(requestDTO.getNextMaintenanceType());
            schedule.setNextDate(requestDTO.getNextDate());
            schedule.setEstimateDuration(requestDTO.getEstimateDuration());
            schedule.setPriority(requestDTO.getPriority());
            schedule.setTechnicianId(technicianId);
            schedule.setLastMaintenanceDate(requestDTO.getLastMaintenanceDate());

            Next_Equipment_Schedule saved = nextEquipmentScheduleRepository.save(schedule);

            NextEquipmentScheduleResponseDTO dto = new NextEquipmentScheduleResponseDTO();
            dto.setNextScheduleId(saved.getNextScheduleId());
            dto.setAssignId(saved.getAssignId());
            dto.setEquipmentId(saved.getEquipmentId());
            dto.setEquipmentScheduleId(saved.getEquipmentScheduleId());
            dto.setNextMaintenanceType(saved.getNextMaintenanceType());
            dto.setNextDate(saved.getNextDate());
            dto.setEstimateDuration(saved.getEstimateDuration());
            dto.setPriority(saved.getPriority());
            dto.setTechnicianId(saved.getTechnicianId());

            responseList.add(dto);
        }

        return responseList;
    }


    public List<NextEquipmentScheduleResponseDTO> getNextScheduleDetailsByAssignId(String assignId) {
        List<Next_Equipment_Schedule> nextSchedules = nextEquipmentScheduleRepository.findByAssignId(assignId);

        return nextSchedules.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NextEquipmentScheduleResponseDTO> getAllNextScheduleDetails() {
        List<Next_Equipment_Schedule> nextSchedules = nextEquipmentScheduleRepository.findAll();

        return nextSchedules.stream()
                .collect(Collectors.toMap(
                    Next_Equipment_Schedule::getEquipmentId,
                    schedule -> schedule,
                    (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }



    public NextEquipmentScheduleResponseDTO mapToResponseDTO(Next_Equipment_Schedule nextEquipmentSchedule) {
        return NextEquipmentScheduleResponseDTO.builder()
                .nextScheduleId(nextEquipmentSchedule.getNextScheduleId())
                .assignId(nextEquipmentSchedule.getAssignId())
                .equipmentId(nextEquipmentSchedule.getEquipmentId())
                .equipmentScheduleId(nextEquipmentSchedule.getEquipmentScheduleId())
                .nextMaintenanceType(nextEquipmentSchedule.getNextMaintenanceType())
                .nextDate(nextEquipmentSchedule.getNextDate())
                .estimateDuration(nextEquipmentSchedule.getEstimateDuration())
                .priority(nextEquipmentSchedule.getPriority())
                .technicianId(nextEquipmentSchedule.getTechnicianId())
                .lastMaintenanceDate(nextEquipmentSchedule.getLastMaintenanceDate())
                .build();
    }
}
