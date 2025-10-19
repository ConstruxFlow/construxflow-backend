package com.example.construxflow.repository;

import com.example.construxflow.entity.I_Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInventorySummaryRepository extends JpaRepository<I_Material, Long> {

    // JPQL uses the entity field names, not column names
    @Query("SELECT COALESCE(SUM(m.quantityInStock), 0) FROM I_Material m")
    Long sumQuantityInStock();

    @Query("SELECT COUNT(DISTINCT m.category) FROM I_Material m")
    Long countUniqueCategories();


}