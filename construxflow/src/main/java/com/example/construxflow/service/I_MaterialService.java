package com.example.construxflow.service;

import com.example.construxflow.dto.I_MaterialDTO;
import com.example.construxflow.entity.I_Material;
import com.example.construxflow.repository.I_MaterialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<I_Material> getMaterialById(Long id) {
        return materialRepository.findById(id);
    }

    // Search materials by name
    public List<I_Material> searchMaterialsByName(String name) {
        return materialRepository.findByNameContainingIgnoreCase(name);
    }

    // Update material stock
    @Transactional
    public I_Material updateMaterialStock(Long id, Integer newQuantity) {
        I_Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + id));

        material.setQuantityInStock(newQuantity);
        return materialRepository.save(material);
    }

    // Update material with full DTO
    @Transactional
    public I_Material updateMaterial(Long id, I_MaterialDTO dto) {
        I_Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + id));

        material.setName(dto.getName());
        material.setCategory(dto.getCategory());
        material.setDescription(dto.getDescription());
        material.setQuantityInStock(dto.getQuantityInStock());
        material.setUnitOfMeasure(dto.getUnitOfMeasure());
        material.setReorderLevel(dto.getReorderLevel());
        material.setPurchaseDate(dto.getPurchaseDate());
        material.setExpirationDate(dto.getExpirationDate());
        material.setSupplierName(dto.getSupplierName());

        return materialRepository.save(material);
    }

    // Delete material
    @Transactional
    public boolean deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new IllegalArgumentException("Material not found with id: " + id);
        }

        try {
            materialRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete material: " + e.getMessage());
        }
    }

    // Get materials with low stock
    public List<I_Material> getLowStockMaterials() {
        // You might want to set a default reorder level or make it configurable
        return materialRepository.findByQuantityInStockLessThanEqual(10);
    }


}
