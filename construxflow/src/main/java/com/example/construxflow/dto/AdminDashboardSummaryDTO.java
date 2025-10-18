package com.example.construxflow.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardSummaryDTO {
    private BigDecimal totalRevenue; // sum of subtotal
    private long activeOrders;       // count of phases with status = "Active"
}