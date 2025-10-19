package com.example.construxflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPerformanceUpdateDTO {
    private String on_time_delivery_rate;
    private String quotation_acceptance_rate;
    private String past_orders_completed;
    private String avg_delay_days;
    private String rating_by_site_manager;
    private String number_of_existing_ratings;
}
