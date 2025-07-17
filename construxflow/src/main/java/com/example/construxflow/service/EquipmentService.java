package com.example.construxflow.service;

import com.example.construxflow.dto.EquipmentDTO;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public Equipment addEquipment(EquipmentDTO dto) {
        Equipment equipment = Equipment.builder()
                .type(dto.getType())
                .name(dto.getName())
                .category(dto.getCategory())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .serialNumber(dto.getSerialNumber())
                .condition(dto.getCondition())
                .purchaseDate(dto.getPurchaseDate())
                .purchaseSource(dto.getPurchaseSource())
                .purchaseCost(dto.getPurchaseCost())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .nextMaintenance(dto.getNextMaintenance())
                .notes(dto.getNotes())
                .build();

        return equipmentRepository.save(equipment);
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElse(null);
    }
}
