package com.example.construxflow.entity;

import java.math.BigDecimal;

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
@Table(name = "quotation_req_materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation_req_materials {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long quotationReqId;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    private Materials material;

    private BigDecimal quantity;
    private BigDecimal estimatedCost;

    @ManyToOne
    @JoinColumn(name = "quotation_id", referencedColumnName = "id")
    private Quotation_request quotationRequest;

}
