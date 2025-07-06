package com.example.construxflow.repository;

import com.example.construxflow.entity.Materials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialsRepository extends JpaRepository<Materials, Long> {
    Optional<Materials> findByMaterialNameAndMaterialType(String materialName, String materialType);
}