// src/main/java/com/example/construxflow/service/InventorySummaryService.java
package com.example.construxflow.service;

import com.example.construxflow.dto.AdminInventorySummaryDTO;
import com.example.construxflow.repository.AdminInventorySummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInventorySummaryService {

    private final AdminInventorySummaryRepository repo;

    @Transactional(readOnly = true)
    public AdminInventorySummaryDTO getSummary() {
        Long totalQty = repo.sumQuantityInStock();
        Long uniqueCats = repo.countUniqueCategories();
        return new AdminInventorySummaryDTO(
                totalQty != null ? totalQty : 0L,
                uniqueCats != null ? uniqueCats : 0L
        );
    }
}