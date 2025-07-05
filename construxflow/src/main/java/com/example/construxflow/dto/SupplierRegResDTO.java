package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierRegResDTO {
    private String supplier_id;
    private String name;
    private String company_name;
    private String business_registration_number;
}
