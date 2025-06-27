package com.example.construxflow.entity;

import java.math.BigDecimal;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "raw_materials")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Raw_material {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long raw_material_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    private Materials material;

    private BigDecimal current_quantity;
    private BigDecimal reorder_level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private Project project;

    
}
