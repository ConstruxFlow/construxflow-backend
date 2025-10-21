package com.example.construxflow.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRequestDetailDTO {
    private String equipmentName;
    private String requestedBy;
    private String schedule;
    private String priority;
    private String availability;
    private String comments;
    private List<MaterialItem> materials;
    private String status; // Add this field

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MaterialItem {
        private String name;
        private String desc;
        private String qty;
        private String stock;
        private String notes;
    }
}