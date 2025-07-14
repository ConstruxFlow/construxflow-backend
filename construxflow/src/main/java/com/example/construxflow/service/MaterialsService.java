package com.example.construxflow.service;

import com.example.construxflow.entity.Materials;
import com.example.construxflow.repository.MaterialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialsService {
    @Autowired
    private MaterialsRepository materialsRepository;

    // Create a new material
    public Materials createMaterial(Materials material) {
        return materialsRepository.save(material);
    }

    // Find all materials
    public List<Materials> findAllMaterials() {
        return materialsRepository.findAll();
    }

    // Find material by ID
    public Optional<Materials> findMaterialById(Long id) {
        return materialsRepository.findById(id);
    }

    // Update material
    public Materials updateMaterial(Long id, Materials updatedMaterial) {
        return materialsRepository.findById(id)
                .map(material -> {
                    material.setMaterialName(updatedMaterial.getMaterialName());
                    material.setMaterialType(updatedMaterial.getMaterialType());
                    material.setUnitOfMeasurement(updatedMaterial.getUnitOfMeasurement());
                    return materialsRepository.save(material);
                })
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
    }

    // Delete material by ID
    public void deleteMaterial(Long id) {
        if (materialsRepository.existsById(id)) {
            materialsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Material not found with id: " + id);
        }
    }

    // Find material by name
    public Optional<Materials> findByMaterialName(String materialName) {
        return materialsRepository.findByMaterialName(materialName);
    }

    // Find all materials by name (in case of duplicates)
    public List<Materials> findAllByMaterialName(String materialName) {
        return materialsRepository.findAllByMaterialName(materialName);
    }

    // Find materials by type
    public List<Materials> findByMaterialType(String materialType) {
        return materialsRepository.findByMaterialType(materialType);
    }

    // Check if material exists by name
    public boolean existsByMaterialName(String materialName) {
        return materialsRepository.existsByMaterialName(materialName);
    }
}
