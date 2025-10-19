package com.example.construxflow.dto;

import com.example.construxflow.entity.EquipmentStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatusUpdateDTO {
    private EquipmentStatus status;
}
