package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestMaintenanceMaterialsResponseDTO {
    private String id;
    private String itemId;
    private String itemName;
    private Double quantity;
    private String measurement;
    private String justification;
    private String urgency;
}
