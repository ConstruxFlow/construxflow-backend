package com.example.construxflow.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "quotation_req_delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation_req_delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long quotationReqDeliveryId;

    private String location;
    private String deliveryDate;
    private BigDecimal quantitySplit;

    @ManyToOne
    @JoinColumn(name = "quotation_id", referencedColumnName = "id")
    private Quotation_request quotationRequest;

}
