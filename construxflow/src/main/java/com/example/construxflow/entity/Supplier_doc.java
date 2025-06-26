package com.example.construxflow.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier_docs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier_doc {
    @Id
    private Long doc_id;
    private String doc_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private Supplier supplier;
}
