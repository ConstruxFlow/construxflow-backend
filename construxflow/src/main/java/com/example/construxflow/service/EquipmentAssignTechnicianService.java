package com.example.construxflow.service;

import com.example.construxflow.dto.EquipmentAssignTechnicianRequestDTO;
import com.example.construxflow.dto.EquipmentAssignTechnicianResponseDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.entity.Equipment_Assign_Technician;
import com.example.construxflow.repository.EquipmentAssignTechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EquipmentAssignTechnicianService {

    @Autowired
    private EquipmentAssignTechnicianRepository equipmentAssignTechnicianRepository;

    public List<EquipmentAssignTechnicianResponseDTO> assignTechnicians(EquipmentAssignTechnicianRequestDTO requestDTO) {
        List<EquipmentAssignTechnicianResponseDTO> responses = new ArrayList<>();

        for (String technicianId : requestDTO.getTechnicianIds()) {
            String assignId = "ASSIGN" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

            Equipment_Assign_Technician technician = new Equipment_Assign_Technician();
            technician.setAssignId(assignId);
            technician.setEquipmentId(requestDTO.getEquipmentId());
            technician.setEquipmentSchedulingId(requestDTO.getEquipmentSchedulingId());
            technician.setTechnicianId(technicianId);
            technician.setDuration(requestDTO.getDuration());
            technician.setNotes(requestDTO.getNotes());
            technician.setStartDate(requestDTO.getStartDate());
            technician.setEndDate(requestDTO.getEndDate());
            technician.setStartTime(requestDTO.getStartTime());
            technician.setEndTime(requestDTO.getEndTime());
            technician.setStatus(requestDTO.getStatus());

            Equipment_Assign_Technician saved = equipmentAssignTechnicianRepository.save(technician);

            EquipmentAssignTechnicianResponseDTO response = new EquipmentAssignTechnicianResponseDTO(
                    saved.getAssignId(),
                    saved.getEquipmentId(),
                    saved.getEquipmentSchedulingId(),
                    saved.getTechnicianId(),
                    saved.getDuration(),
                    saved.getNotes(),
                    saved.getStartDate(),
                    saved.getEndDate(),
                    saved.getStartTime(),
                    saved.getEndTime(),
                    saved.getStatus()
            );

            responses.add(response);
        }

        return responses;
    }


    public List<EquipmentAssignTechnicianResponseDTO> getEquipmentAssignDetails(){
        return equipmentAssignTechnicianRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
//
//    public Optional<EquipmentAssignTechnicianResponseDTO> getEquipmentSchedulingDetailsById(String schedulingId){
//        return equipmentAssignTechnicianRepository.findByEquipmentSchedulingId(schedulingId)
//                .map(this::mapToResponseDTO);
//
//    }

    public List<EquipmentAssignTechnicianResponseDTO> getAssignmentsByScheduleId(String scheduleId) {
        List<Equipment_Assign_Technician> assignments = equipmentAssignTechnicianRepository.findByEquipmentSchedulingId(scheduleId);
        return assignments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    public EquipmentAssignTechnicianResponseDTO updateAssignStatus(String id, String newStatus){
        Equipment_Assign_Technician assignTechnician = equipmentAssignTechnicianRepository.findById(id).
                orElseThrow(()->new RuntimeException("Equipment assign technician  not found with id: " + id));
        assignTechnician.setStatus(newStatus);
        equipmentAssignTechnicianRepository.save(assignTechnician);
        return mapToResponseDTO(assignTechnician);
    }

    public List<EquipmentAssignTechnicianResponseDTO> getAssignmentsByTechnicianId(String technicianId) {
        List<Equipment_Assign_Technician> assignments = equipmentAssignTechnicianRepository.findByTechnicianId(technicianId);
        return assignments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public EquipmentAssignTechnicianResponseDTO mapToResponseDTO(Equipment_Assign_Technician equipmentAssignTechnician) {
        return EquipmentAssignTechnicianResponseDTO.builder()
                .assignId(equipmentAssignTechnician.getAssignId())
                .equipmentId(equipmentAssignTechnician.getEquipmentId())
                .equipmentSchedulingId(equipmentAssignTechnician.getEquipmentSchedulingId())
                .technicianId(equipmentAssignTechnician.getTechnicianId())
                .duration(equipmentAssignTechnician.getDuration())
                .notes(equipmentAssignTechnician.getNotes())
                .startDate(equipmentAssignTechnician.getStartDate())
                .endDate(equipmentAssignTechnician.getEndDate())
                .startTime(equipmentAssignTechnician.getStartTime())
                .endTime(equipmentAssignTechnician.getEndTime())
                .status(equipmentAssignTechnician.getStatus())
                .build();
    }
}
