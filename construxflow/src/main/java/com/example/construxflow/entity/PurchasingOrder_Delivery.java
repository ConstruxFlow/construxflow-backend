package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchasingorder_delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasingOrder_Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate requiredDate;
    private String location;
    private BigDecimal shippingCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id",referencedColumnName = "po_id")
    private PurchasingOrder purchasingorder;
}
