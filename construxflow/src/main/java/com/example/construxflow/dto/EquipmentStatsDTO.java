package com.example.construxflow.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EquipmentStatsDTO {
    private long total;
    private long available;
    private long onASite;
    private long underMaintenance;
}
