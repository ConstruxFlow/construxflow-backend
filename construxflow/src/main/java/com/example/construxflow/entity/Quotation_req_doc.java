package com.example.construxflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.mapping.Join;

import jakarta.annotation.Generated;
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
@Table(name = "quotation_req_doc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation_req_doc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quotationReqId;
    private String documentName;
    private String documentType;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "quotation_id", referencedColumnName = "id")
    @JsonIgnore
    private Quotation_request quotationRequest;

}
