package com.example.construxflow.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class MaterialInventoryUpdateDTO {

        private String materialRequestId;
        private String materialName;
        private Double requestedQuantity;
        private Boolean updateInventory;
        private String notes;
    }

