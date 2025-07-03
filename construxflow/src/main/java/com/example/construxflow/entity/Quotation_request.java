package com.example.construxflow.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quotation_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation_request {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String requesterName;
    private Date request_date;
    private Date Quotation_deadline;
    private String priority_level;
    private String status;
    private String additional_info;
    private String quotation_type; // e.g., "Material", "Service", "Equipment"
    private BigDecimal estimated_cost;
    
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToMany(mappedBy = "quotationRequest", cascade = CascadeType.ALL)
    private List<Quotation_req_materials> quotationReqMaterials;

    @OneToMany(mappedBy = "quotationRequest", cascade = CascadeType.ALL)
    private List<Quotation_req_delivery> quotationReqDelivery;

    @OneToMany(mappedBy = "quotationRequest", cascade = CascadeType.ALL)
    private List<Quotation_req_doc> quotationReqDocs;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "manager_id")
    private Manager manager;



}
