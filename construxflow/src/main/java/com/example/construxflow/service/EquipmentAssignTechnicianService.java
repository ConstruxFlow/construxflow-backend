package com.example.construxflow.service;

import com.example.construxflow.dto.EquipmentAssignTechnicianRequestDTO;
import com.example.construxflow.dto.EquipmentAssignTechnicianResponseDTO;
import com.example.construxflow.dto.EquipmentSchedulingResponseDTO;
import com.example.construxflow.entity.Equipment_Assign_Technician;
import com.example.construxflow.repository.EquipmentAssignTechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EquipmentAssignTechnicianService {

    @Autowired
    private EquipmentAssignTechnicianRepository equipmentAssignTechnicianRepository;

    public EquipmentAssignTechnicianResponseDTO assignTechnician(EquipmentAssignTechnicianRequestDTO requestDTO) {

        String AssignId = "ASSIGN"+ UUID.randomUUID().toString().substring(0,5).toUpperCase();

        Equipment_Assign_Technician equipmentAssignTechnician = new Equipment_Assign_Technician();
        equipmentAssignTechnician.setAssignId(AssignId);
        equipmentAssignTechnician.setEquipmentSchedulingId(requestDTO.getEquipmentSchedulingId());
        equipmentAssignTechnician.setTechnicianId(requestDTO.getTechnicianId());
        equipmentAssignTechnician.setDuration(requestDTO.getDuration());
        equipmentAssignTechnician.setNotes(requestDTO.getNotes());
        equipmentAssignTechnician.setStartDate(requestDTO.getStartDate());
        equipmentAssignTechnician.setEndDate(requestDTO.getEndDate());
        equipmentAssignTechnician.setStartTime(requestDTO.getStartTime());
        equipmentAssignTechnician.setEndTime(requestDTO.getEndTime());
        equipmentAssignTechnician.setStatus(requestDTO.getStatus());

        Equipment_Assign_Technician savedAssign = equipmentAssignTechnicianRepository.save(equipmentAssignTechnician);

        EquipmentAssignTechnicianResponseDTO responseDTO = new EquipmentAssignTechnicianResponseDTO();
        responseDTO.setAssignId(savedAssign.getAssignId());
        responseDTO.setEquipmentSchedulingId(savedAssign.getEquipmentSchedulingId());
        responseDTO.setTechnicianId(savedAssign.getTechnicianId());
        responseDTO.setDuration(savedAssign.getDuration());
        responseDTO.setNotes(savedAssign.getNotes());
        responseDTO.setStartDate(savedAssign.getStartDate());
        responseDTO.setEndDate(savedAssign.getEndDate());
        responseDTO.setStartTime(savedAssign.getStartTime());
        responseDTO.setEndTime(savedAssign.getEndTime());
        responseDTO.setStatus(savedAssign.getStatus());
        return responseDTO;
    }

    public List<EquipmentAssignTechnicianResponseDTO> getEquipmentAssignDetails(){
        return equipmentAssignTechnicianRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EquipmentAssignTechnicianResponseDTO> getEquipmentSchedulingDetailsById(String schedulingId){
        return equipmentAssignTechnicianRepository.findByEquipmentSchedulingId(schedulingId)
                .map(this::mapToResponseDTO);

    }

    public Optional<EquipmentAssignTechnicianResponseDTO> getEquipmentSchedulingDetailsByAssignId(String assignId){
        return equipmentAssignTechnicianRepository.findById(assignId)
                .map(this::mapToResponseDTO);
    }

    public EquipmentAssignTechnicianResponseDTO updateAssignStatus(String id, String newStatus){
        Equipment_Assign_Technician assignTechnician = equipmentAssignTechnicianRepository.findById(id).
                orElseThrow(()->new RuntimeException("Equipment assign technician  not found with id: " + id));
        assignTechnician.setStatus(newStatus);
        equipmentAssignTechnicianRepository.save(assignTechnician);
        return mapToResponseDTO(assignTechnician);
    }

    public EquipmentAssignTechnicianResponseDTO mapToResponseDTO(Equipment_Assign_Technician equipmentAssignTechnician) {
        return EquipmentAssignTechnicianResponseDTO.builder()
                .assignId(equipmentAssignTechnician.getAssignId())
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
