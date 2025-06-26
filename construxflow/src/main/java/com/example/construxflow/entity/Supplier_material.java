package com.example.construxflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "supplier_materials")
public class Supplier_material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long material_id;

    private String material_name;
    private String material_type;
    private String test_report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private Supplier supplier;
}
