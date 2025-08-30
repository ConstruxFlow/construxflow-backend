package com.example.construxflow.service;

import com.example.construxflow.dto.I_MaterialDTO;
import com.example.construxflow.entity.I_Material;
import com.example.construxflow.repository.I_MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class I_MaterialService {

    @Autowired
    private I_MaterialRepository materialRepository;

    public I_Material addMaterial(I_MaterialDTO dto) {
        I_Material material = I_Material.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .quantityInStock(dto.getQuantityInStock())
                .unitOfMeasure(dto.getUnitOfMeasure())
                .reorderLevel(dto.getReorderLevel())
                .purchaseDate(dto.getPurchaseDate())
                .expirationDate(dto.getExpirationDate())
                .supplierName(dto.getSupplierName())
                .build();

        return materialRepository.save(material);
    }

    public List<I_Material> getAllMaterials() {
        return materialRepository.findAll();
    }

}
