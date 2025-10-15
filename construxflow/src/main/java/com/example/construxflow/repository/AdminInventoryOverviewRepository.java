package com.example.construxflow.repository;

import com.example.construxflow.entity.I_Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminInventoryOverviewRepository extends JpaRepository<I_Material, Long> {

    Page<I_Material> findByCategory(String category, Pageable pageable);

    @Query("""
        SELECT m.category AS category,
               SUM(COALESCE(m.quantityInStock, 0)) AS totalQuantity,
               SUM(COALESCE(m.reorderLevel, 0)) AS totalReorderLevel,
               COUNT(m) AS materialsCount
        FROM I_Material m
        GROUP BY m.category
    """)
    List<CategorySummaryProjection> summarizeByCategory();

    @Query("""
        SELECT m
        FROM I_Material m
        WHERE m.reorderLevel IS NOT NULL
          AND m.reorderLevel > 0
          AND m.quantityInStock < m.reorderLevel
    """)
    List<I_Material> findCriticalMaterials();

    // Nested projection (implicitly public and static inside an interface)
    interface CategorySummaryProjection {
        String getCategory();
        Long getTotalQuantity();
        Long getTotalReorderLevel();
        Long getMaterialsCount();
    }
}