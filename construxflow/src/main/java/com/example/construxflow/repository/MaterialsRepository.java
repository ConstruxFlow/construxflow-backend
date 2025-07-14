package com.example.construxflow.repository;

import com.example.construxflow.entity.Materials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialsRepository extends JpaRepository<Materials, Long> {
    Optional<Materials> findByMaterialNameAndMaterialType(String materialName, String materialType);

    // Find by material name
    Optional<Materials> findByMaterialName(String materialName);

    // Find all materials by material name (in case of duplicates)
    List<Materials> findAllByMaterialName(String materialName);

    // Find by material type
    List<Materials> findByMaterialType(String materialType);

    // Check if material exists by name
    boolean existsByMaterialName(String materialName);
}