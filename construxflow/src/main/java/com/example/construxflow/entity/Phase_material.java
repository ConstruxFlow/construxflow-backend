package com.example.construxflow.entity;

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
@Table(name = "phase_materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phase_material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long phase_material_id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    private Materials material;

    private Long quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", referencedColumnName = "phase_id")
    private Project_phase project_phase;

}
