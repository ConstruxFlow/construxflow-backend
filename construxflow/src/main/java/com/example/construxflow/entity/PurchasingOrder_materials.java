package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "purchasingorder_materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasingOrder_materials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchasingOrderMaterialId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    private Materials material;

    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal Cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id",referencedColumnName = "po_id")
    private PurchasingOrder purchasingorder;

}
