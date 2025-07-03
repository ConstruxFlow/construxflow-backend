package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRegReqDTO {
    private String supplier_id;
    private String name;
    private String company_name;
    private String business_registration_number;
}
