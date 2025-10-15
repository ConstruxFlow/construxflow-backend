package com.example.construxflow.service;

import com.example.construxflow.dto.AdminInventoryOverviewDTO;
import com.example.construxflow.dto.AdminInventoryOverviewDTO.CriticalItemDTO;
import com.example.construxflow.dto.AdminInventoryOverviewDTO.StockItemDTO;
import com.example.construxflow.entity.I_Material;
import com.example.construxflow.repository.AdminInventoryOverviewRepository;
import com.example.construxflow.repository.AdminInventoryOverviewRepository.CategorySummaryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInventoryOverviewService {

    private final AdminInventoryOverviewRepository repository;

    public AdminInventoryOverviewDTO getOverview() {
        List<StockItemDTO> stock = repository.summarizeByCategory().stream()
                .map(this::toStockItem)
                .sorted(Comparator.comparing(
                        StockItemDTO::getCategory,
                        Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toList());

        List<CriticalItemDTO> critical = getCriticalItems();

        return AdminInventoryOverviewDTO.builder()
                .stockItems(stock)
                .criticalItems(critical)
                .build();
    }

    public List<CriticalItemDTO> getCriticalItems() {
        return repository.findCriticalMaterials().stream()
                .map(this::toCriticalItem)
                .sorted(Comparator
                        .comparing(CriticalItemDTO::getPriority)  // "high" before "medium"
                        .thenComparing(CriticalItemDTO::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public Page<I_Material> getMaterials(Pageable pageable, String category) {
        if (category != null && !category.isBlank()) {
            return repository.findByCategory(category, pageable);
        }
        return repository.findAll(pageable);
    }

    private StockItemDTO toStockItem(CategorySummaryProjection p) {
        long totalQty = p.getTotalQuantity() != null ? p.getTotalQuantity() : 0L;
        long totalReorder = p.getTotalReorderLevel() != null ? p.getTotalReorderLevel() : 0L;
        long recommended = totalReorder > 0 ? totalReorder * 2 : 0L;

        int capacityPercent;
        String status;

        if (totalReorder <= 0) {
            capacityPercent = 100;
            status = "Good";
        } else {
            double ratioToRecommended = recommended > 0 ? (double) totalQty / (double) recommended : 1.0;
            capacityPercent = (int) Math.round(Math.max(0d, Math.min(1d, ratioToRecommended)) * 100d);

            if (totalQty < totalReorder) {
                status = "Critical";
            } else if (totalQty < recommended) {
                status = "Low";
            } else {
                status = "Good";
            }
        }

        return StockItemDTO.builder()
                .category(p.getCategory())
                .totalQuantity(totalQty)
                .totalReorderLevel(totalReorder)
                .recommendedLevel(recommended)
                .capacityPercent(capacityPercent)
                .status(status)
                .materialsCount(p.getMaterialsCount() != null ? p.getMaterialsCount() : 0L)
                .build();
    }

    private CriticalItemDTO toCriticalItem(I_Material m) {
        int current = m.getQuantityInStock() != null ? m.getQuantityInStock() : 0;
        int min = m.getReorderLevel() != null ? m.getReorderLevel() : 0;

        String priority;
        if (min <= 0) {
            priority = "medium";
        } else {
            double ratio = (double) current / (double) min;
            priority = ratio < 0.5 ? "high" : "medium";
        }

        return CriticalItemDTO.builder()
                .id(m.getId())
                .name(m.getName())
                .category(m.getCategory())
                .current(current)
                .min(min)
                .unitOfMeasure(m.getUnitOfMeasure())
                .priority(priority)
                .build();
    }
}