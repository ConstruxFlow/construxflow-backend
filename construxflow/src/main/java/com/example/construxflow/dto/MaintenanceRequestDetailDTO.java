package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRequestDetailDTO {
    private String equipmentName;
    private String requestedBy;
    private String schedule;
    private String priority;
    private String availability;
    private String comments;
    private List<MaterialItem> materials;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MaterialItem {
        private String name;
        private String desc;
        private String qty;
        private String stock;
        private String notes;
    }
}
