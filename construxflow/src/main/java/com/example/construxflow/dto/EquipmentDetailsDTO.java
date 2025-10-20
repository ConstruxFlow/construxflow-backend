package com.example.construxflow.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentDetailsDTO {
    private Long id;
    private String name;
    private String type;
    private String brand;
    private String model;
    private String status;
    private String location;
    private String lastMaintenance;
    private String nextMaintenance;
    private String utilization;
    private String specifications;
    private String notes;
}
