package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "quotation_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quotation_id" , referencedColumnName = "id")
    private Quotation quotation;

    private String itemName;
    private int quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

}
