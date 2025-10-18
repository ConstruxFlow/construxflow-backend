// src/main/java/com/example/construxflow/service/AdminDashboardSummaryService.java
package com.example.construxflow.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.construxflow.dto.AdminDashboardSummaryDTO;
import com.example.construxflow.repository.AdminDashboardSummaryRepository;

@Service
@RequiredArgsConstructor
public class AdminDashboardSummaryService {

    private final AdminDashboardSummaryRepository repo;

    @Transactional(readOnly = true)
    public AdminDashboardSummaryDTO getSummary() {
        BigDecimal total = repo.sumOfSubtotals();
        if (total == null) total = BigDecimal.ZERO;

        long active = repo.countActivePhases(); // handles case/whitespace

        return new AdminDashboardSummaryDTO(total, active);
    }
}