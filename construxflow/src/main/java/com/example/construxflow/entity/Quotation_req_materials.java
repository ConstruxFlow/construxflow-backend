package com.example.construxflow.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long quotationReqMaterialId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    private Materials material;

    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal estimatedCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", referencedColumnName = "id")
    @JsonIgnore
    private Quotation_request quotationRequest;

}
