// src/main/java/com/example/construxflow/repository/AdminDashboardSummaryRepository.java
package com.example.construxflow.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.construxflow.entity.Project_phase;

@Repository
public interface AdminDashboardSummaryRepository extends JpaRepository<Project_phase, Long> {

    @Query("SELECT COALESCE(SUM(p.subtotal), 0) FROM Project_phase p")
    BigDecimal sumOfSubtotals();

    @Query("SELECT COUNT(p) FROM Project_phase p WHERE UPPER(TRIM(p.status)) = 'ACTIVE'")
    long countActivePhases();

    // If you prefer a derived method (won't trim spaces):
    // long countByStatusIgnoreCase(String status);
}