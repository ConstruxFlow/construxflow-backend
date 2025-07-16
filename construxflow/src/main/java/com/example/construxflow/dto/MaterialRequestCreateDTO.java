package com.example.construxflow.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestCreateDTO {
    private String project; // Project identifier or name
    private String phase;   // Phase identifier or name
    private String requestDate;
    private String notes;
    private String priority;
    private List<MaterialItemDTO> materials; // Multiple materials
} 