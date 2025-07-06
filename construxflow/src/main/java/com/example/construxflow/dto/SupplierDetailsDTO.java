package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierDetailsDTO {
    private String supplier_id;


    private String name;
    private String company_name;
    private String Business_Registration_Number;
    private String Delivery_Capabilities;
    private String status;
    private String bank_name;
    private String bank_account_name;
    private String bank_account_number;
    private String email;
    private Long phone_number1;
    private Long phone_number2;
    private String address;
    private String on_time_delivery_rate;
    private String quotation_acceptance_rate;
    private Long past_orders_completed;
    private Long avg_delay_days;
    private Long rating_by_site_manager;
}
