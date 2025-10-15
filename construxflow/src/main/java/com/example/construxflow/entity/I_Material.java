package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "i_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class I_Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String category;

    @Column(length = 1000)
    private String description;

    private Integer quantityInStock;
    private String unitOfMeasure;
    private Integer reorderLevel;
    private String purchaseDate;
    private String expirationDate;
    private String supplierName;



}
