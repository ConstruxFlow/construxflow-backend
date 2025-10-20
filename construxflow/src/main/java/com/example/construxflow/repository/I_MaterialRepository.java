package com.example.construxflow.repository;

import com.example.construxflow.entity.I_Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface I_MaterialRepository extends JpaRepository<I_Material, Long>, JpaSpecificationExecutor<I_Material> {

    // Find material by name (exact match)
    Optional<I_Material> findByName(String name);

    // Search materials by name containing (case insensitive)
    List<I_Material> findByNameContainingIgnoreCase(String name);

    // Update stock quantity
    @Modifying
    @Query("UPDATE I_Material m SET m.quantityInStock = :newQuantity WHERE m.id = :id")
    void updateStockQuantity(@Param("id") Long id, @Param("newQuantity") Integer newQuantity);

    // Find materials with low stock (below reorder level)
    List<I_Material> findByQuantityInStockLessThanEqual(Integer reorderLevel);

    boolean existsById(Long id);


    @Query("SELECT COUNT(m) FROM I_Material m")
    Long countTotalMaterials();

    @Query("SELECT COUNT(m) FROM I_Material m WHERE m.quantityInStock <= m.reorderLevel")
    Long countLowStockMaterials();

}
