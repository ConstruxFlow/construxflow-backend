package com.example.construxflow.dto;

import lombok.Data;

@Data
public class EquipmentStatusDTO {
    private String status;
    private Long count;
    private String color;
    private Double percentage;
}