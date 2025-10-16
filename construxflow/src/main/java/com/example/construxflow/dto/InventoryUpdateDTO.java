package com.example.construxflow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryUpdateDTO {
    private String type; // "equipment" or "material"
    private Long id;
    private String action; // "add" or "remove"
    private Integer quantity;
    private String name; // for search functionality
}