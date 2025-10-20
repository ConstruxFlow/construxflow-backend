package com.example.construxflow.repository;

import com.example.construxflow.entity.I_Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<I_Material, Long> {
    Long countByQuantityInStockLessThanEqual(Integer threshold);

    @Query("SELECT m FROM I_Material m WHERE m.quantityInStock <= m.reorderLevel")
    List<I_Material> findLowStockMaterials();
}