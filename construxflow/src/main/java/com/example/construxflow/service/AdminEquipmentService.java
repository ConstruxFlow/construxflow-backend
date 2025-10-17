package com.example.construxflow.service;

import com.example.construxflow.dto.AdminEquipmentSummaryDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<AdminEquipmentSummaryDTO> getEquipmentSummaries() {
        return equipmentRepository.findAll()
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    private AdminEquipmentSummaryDTO toSummary(Equipment e) {
        return AdminEquipmentSummaryDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .status(e.getStatus())
                // Works whether lastMaintenance is String/LocalDate/LocalDateTime in the entity
                .lastMaintenance(e.getLastMaintenance() != null ? e.getLastMaintenance().toString() : null)
                .build();
    }
}